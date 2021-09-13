declare namespace wx {
  function requestSubscribeMessage(params: {
    tmplIds: string[];
    success?: (prams: { errMsg: string; TEMPLATE_ID: string }) => void;
    fail?: (error: any) => any;
    complete?: () => any;
  }): void;

  function getFileSystemManager(data: any): any;

  function getFileSystemManager(): any;

  function base64ToArrayBuffer(bodyData: any): any;

  const env: any;
}
