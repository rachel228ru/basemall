package com.medusa.gruul.common.redis.parse;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.redis.annotation.Hash;
import com.medusa.gruul.common.redis.annotation.HashColumn;
import com.medusa.gruul.common.redis.exception.InvalidParameterException;
import com.medusa.gruul.common.redis.exception.MissAnnotationException;
import com.medusa.gruul.common.redis.model.KeyMapPair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * hash map parser 解析器
 * <p/>
 * v1:支持基本类型+date类型的转换 TODO clz v2:支持引用类型属性的转换,使用新hash作为处理原则
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 16/6/24 上午9:52
 */
public final class HashMapParser {

	public static final Logger LOG = LoggerFactory.getLogger("redis-hashmap-parser");

	/**
	 * key参数前缀
	 */
	public static final String KEY_PARAM_PREFIX = "{";

	public static final String KEY_PARAM_PREFIX_REPLACE = "\\{";

	/**
	 * key参数后缀
	 */
	public static final String KEY_PARAM_SUFFIX = "}";

	public static final String KEY_PARAM_SUFFIX_REPLACE = "\\}";

	/**
	 * key map pair 转换为对象
	 *
	 * @param t
	 *            需要转换的实例对象
	 * @param hash
	 * @param <T>
	 * @return
	 */
	public static <T> T hash2Obj(T t, Map<String, String> hash) throws InvalidParameterException {

		if (t == null) {
			throw new InvalidParameterException("format obj can not null");
		}
		if (null == hash || 0 == hash.size()) {
			throw new InvalidParameterException("hash can not null");
		}

		Class<?> clazz = t.getClass();

		Field[] fields = clazz.getDeclaredFields();
		if (ArrayUtils.isEmpty(fields)) {
			return null;
		}

		for (Field field : fields) {
			setVal(t, field, hash);
		}

		return t;
	}

	public static <T> void setVal(T t, Field field, Map<String, String> hash) {
		HashColumn hashColumn = field.getAnnotation(HashColumn.class);

		if (hashColumn == null) {
			return;
		}

		String value = null;

		String columnName = hashColumn.name();

		Object param = null;

		if (hash.containsKey(columnName)) {
			// 有值
			value = hash.get(columnName);

			try {
				Class<?> fieldClazz = field.getType();

				if (value != null && !"null".equals(value)) {
					if (String.class == fieldClazz) {
						param = value;
					} else {
						if (long.class == fieldClazz || Long.class == fieldClazz) {
							param = Long.valueOf(value);
						} else if (int.class == fieldClazz || Integer.class == fieldClazz) {
							param = Integer.valueOf(value);
						} else if (boolean.class == fieldClazz || Boolean.class == fieldClazz) {
							param = Boolean.valueOf(value);
						} else if (Date.class == fieldClazz) {
							// 转为时间戳
							param = new Date(Long.valueOf(value));
						} else if (double.class == fieldClazz || Double.class == fieldClazz) {
							param = Double.valueOf(value);
						} else if (float.class == fieldClazz || Float.class == fieldClazz) {
							param = Float.valueOf(value);
						}
					}
					t.getClass().getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), fieldClazz)
							.invoke(t, param);
				}
			} catch (IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				LOG.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 对象 转换为 redis的map
	 *
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> KeyMapPair obj2Hash(T obj) throws MissAnnotationException {
		if (null == obj) {
			return null;
		}

		Class<?> clazz = obj.getClass();

		// hashMap annotation
		Hash hashAnnotation = clazz.getAnnotation(Hash.class);
		if (null == hashAnnotation) {
			throw new MissAnnotationException(clazz.getCanonicalName(), Hash.class.getCanonicalName());
		}

		// 属性
		Field[] fields = clazz.getDeclaredFields();

		Map<String, String> hash = new HashMap<>();
		if (ArrayUtils.isNotEmpty(fields)) {
			for (Field field : fields) {
				// hash map 列名
				HashColumn hashColumn = field.getAnnotation(HashColumn.class);

				if (hashColumn != null) {
					String val = getFieldStr(obj, field);
					if (StringUtils.isNotEmpty(val)) {
						// 解析列
						hash.put(hashColumn.name(), val);
					}
				}
			}
		}

		String key = parseKey(clazz, hashAnnotation.key(), hash);

		KeyMapPair keyMapPair = new KeyMapPair();
		keyMapPair.setKey(key);
		keyMapPair.setMap(hash);

		return keyMapPair;
	}

	/**
	 * 解析hash的key
	 *
	 * @param clazz
	 * @param annoKey
	 * @param hash
	 * @return
	 */
	protected static String parseKey(Class<?> clazz, String annoKey, Map<String, String> hash)
			throws MissAnnotationException {
		// user:{account}:his:{day}
		if ("".equals(annoKey.trim())) {
			throw new MissAnnotationException(
					"class:[" + clazz.getCanonicalName() + "] hash annotation key can not be empty");
		}

		if (annoKey.contains(KEY_PARAM_PREFIX) && (null == hash || 0 < hash.size())) {
			throw new MissAnnotationException("class:[" + clazz.getCanonicalName() + "] hash annotation key:[" + annoKey
					+ "]has param key,but column annos is null");
		}

		String key = annoKey;
		// key参数
		while (key.contains(KEY_PARAM_PREFIX)) {
			// 开始
			int begin = key.indexOf(KEY_PARAM_PREFIX);
			// 结束
			int end = key.indexOf(KEY_PARAM_SUFFIX);
			// 检测符号对
			if (end < begin) {
				// key参数符号对异常
				throw new MissAnnotationException(
						"class:[" + clazz.getCanonicalName() + "] hash annotation key param fix error");
			}

			String oneKey = key.substring(begin + 1, end);

			String value = hash.get(oneKey);
			if (StringUtils.isEmpty(value)) {
				// value不存在则设置为
				value = "";
			}

			// 替换key参数
			key = key.replaceAll(KEY_PARAM_PREFIX_REPLACE + oneKey + KEY_PARAM_SUFFIX_REPLACE, value);

		}

		// LOG.info(key);
		return key;
	}

	/**
	 * 获取属性字符串
	 *
	 * @param obj
	 * @param field
	 * @param <T>
	 * @return
	 */
	protected static <T> String getFieldStr(T obj, Field field) {
		String str = null;
		try {
			Object o = obj.getClass().getMethod("get" + StringUtils.capitalize(field.getName())).invoke(obj);

			if (o != null) {
				// 属性类型
				Class<?> fieldClazz = field.getType();

				if (String.class == fieldClazz) {
					str = String.valueOf(o);
				} else if (long.class == fieldClazz || Long.class == fieldClazz) {
					str = String.valueOf(o);
				} else if (int.class == fieldClazz || Integer.class == fieldClazz) {
					str = String.valueOf(o);
				} else if (boolean.class == fieldClazz || Boolean.class == fieldClazz) {
					str = String.valueOf(o);
				} else if (Date.class == fieldClazz) {
					// 转为时间戳
					Date d = (Date) o;
					str = String.valueOf(d.getTime());
				} else if (double.class == fieldClazz || Double.class == fieldClazz) {
					str = String.valueOf(o);
				} else if (float.class == fieldClazz || Float.class == fieldClazz) {
					str = String.valueOf(o);
				}
			}
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			LOG.error(e.getMessage(), e);
		}
		return str;
	}

	public static String map2Json(Map<String, String> map) {

		if (map == null || map.isEmpty()) {
			return StringUtils.EMPTY;
		}

		JSONObject jsonObject = new JSONObject();
		for (Entry<String, String> param : map.entrySet()) {
			jsonObject.put(param.getKey(), param.getValue());
		}

		return jsonObject.toJSONString();
	}


	public static byte[] serizlize(Object object){
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(baos != null){
					baos.close();
				}
				if (oos != null) {
					oos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}



	public static <T> T  deserialize(byte[] bytes,T t){
		if (t == null) {
			throw new InvalidParameterException("format obj can not null");
		}
		if (null == bytes || 0 == bytes.length) {
			throw new InvalidParameterException("bytes can not null");
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try{

			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			Object readObject = ois.readObject();
			return (T)readObject;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

}
