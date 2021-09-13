import axios, { AxiosInstance, AxiosRequestConfig } from "axios";

/**
 * @description ajax res基础返回体
 * @export
 * @interface IBaseResponse
 * @template T
 */
export interface IBaseResponse<T = any> {
  code: number;
  data: T;
  msg?: string;
  [x:string]:any
}

class HttpRequest {
  public instance: AxiosInstance;

  private baseURL: string;

  private headers: any;

  constructor({ baseURL, headers = {} }: AxiosRequestConfig) {
    this.baseURL = baseURL;
    this.headers = headers;
    this.instance = axios.create();
  }

  get<T = any>(url: string, data: any, config: AxiosRequestConfig = {}) {
    return this.request<T>({
      url,
      method: "get",
      params: data,
      ...config,
    });
  }

  post<T = any>(url: string, data: any, config: AxiosRequestConfig = {}) {
    return this.request<T>({
      url,
      method: "post",
      data,
      ...config,
    });
  }

  put<T = any>(url: string, data: any, config: AxiosRequestConfig = {}) {
    return this.request<T>({
      url,
      method: "put",
      data,
      ...config,
    });
  }

  delete<T = any>(url: string, data: any, config: AxiosRequestConfig = {}) {
    return this.request<T>({
      url,
      method: "delete",
      params: data,
      ...config,
    });
  }

  public getInsideConfig() {
    const config = {
      baseURL: this.baseURL,
      headers: this.headers,
    };
    return config;
  }

  public interceptors(instance: AxiosInstance) {
    // 请求拦截
    instance.interceptors.request.use(
      config => {
        return config;
      },
      error => {
        return Promise.reject(error);
      },
    );
    // 响应拦截
    instance.interceptors.response.use(
      res => {
        return res;
      },
      error => {
        return Promise.reject(error);
      },
    );
  }

  public request<T = any>(options: AxiosRequestConfig) {
    options = Object.assign(this.getInsideConfig(), options);
    this.interceptors(this.instance);
    return this.instance.request<T, IBaseResponse<T>>(options);
  }
}

export default HttpRequest;
