//SHA256加密
import CryptoJS from 'crypto-js';
export const SHA256 = (password) => {
  let hash = CryptoJS.SHA256(password);
  //转为16进制字符串
  hash = hash.toString(CryptoJS.enc.Hex);
  return hash;
}