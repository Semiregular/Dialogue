import Cookies from "js-cookie";

export const getCookie = (key) => {
  //字符化
  let json = JSON.stringify({ [key]: Cookies.get(key) });
  //转化为对象
  let obj = JSON.parse(json);
  return obj;
};

export const setCookie = (key, value) => {
  //匹配\
  let reg1 = /\\/g; 
  //匹配开始和结束的"
  var reg2 = /^['|"](.*)['|"]$/;
  //字符化
  let json = JSON.stringify(value);
  json = json.replace(reg1, "");
  json = json.replace(reg2, "$1");
  json = json.replace(reg2, "$1");
  //3天后过期
  return Cookies.set(key, json, { expires: 3 });
};

export const removeCookie = (key) => {
  return Cookies.remove(key);
};
