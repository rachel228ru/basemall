export const readFile = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const fileReader: FileReader = new FileReader();
    fileReader.onload = ({ target: { result } }: any) => {
      resolve(result);
    };
    fileReader.onerror = reject;
    fileReader.readAsDataURL(file);
  });
};

export const advanceGetImage = (
  url: string,
): Promise<{ image: HTMLImageElement; ev: any }> => {
  return new Promise((resolve, reject) => {
    const image = new Image();
    image.onload = ev => {
      resolve({ image, ev });
    };
    image.onerror = reject;
    image.src = url;
  });
};
