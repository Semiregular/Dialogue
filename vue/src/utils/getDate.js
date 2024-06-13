//返回现在的datetime,格式为"yyyy-mm-dd hh:mm:ss"
export const getDate = () => {
  var date = new Date();
  var sign = ":";
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  var hour = date.getHours();
  var minute = date.getMinutes();
  var second = date.getSeconds();

  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (day >= 0 && day <= 9) {
    day = "0" + day;
  }
  if (hour >= 0 && hour <= 9) {
    hour = "0" + hour;
  }
  if (minute >= 0 && minute <= 9) {
    minute = "0" + minute;
  }
  if (second >= 0 && second <= 9) {
    second = "0" + second;
  }
  return (
    year + "-" + month + "-" + day + " " + hour + sign + minute + sign + second
  );
};

//01234567890123456789
//yyyy-mm-dd hh:mm:ss
//GMT+8:00 转化为相对时间
export const relativeDate = (rawdate) => {
  var rawyear = rawdate.slice(0, 4);
  var rawmonth = rawdate.slice(5, 7);
  var rawday = rawdate.slice(8, 10);
  var rawhour = rawdate.slice(11, 13);
  var rawminute = rawdate.slice(14, 16);
  var rawsecond = rawdate.slice(17, 19);
  var date = new Date();
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  var hour = date.getHours();
  var minute = date.getMinutes();
  var second = date.getSeconds();

  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (day >= 0 && day <= 9) {
    day = "0" + day;
  }
  if (hour >= 0 && hour <= 9) {
    hour = "0" + hour;
  }
  if (minute >= 0 && minute <= 9) {
    minute = "0" + minute;
  }
  if (second >= 0 && second <= 9) {
    second = "0" + second;
  }

  if (rawyear == year) {
    if (rawmonth == month && rawday == day) {
      if (rawhour == hour) {
        if (rawminute == minute) {
          var before = parseInt(rawsecond, 10);
          var after = parseInt(second, 10);
          var dif = after - before;
          return dif + "秒前";
        } else {
          var before = parseInt(rawminute, 10);
          var after = parseInt(minute, 10);
          var dif = after - before;
          return dif + "分钟前";
        }
      } else {
        return rawdate.slice(11, 16);
      }
    } else {
      rawmonth = parseInt(rawmonth,10)
      rawday = parseInt(rawday,10)
      return rawmonth + "月" + rawday + "日";
    }
  } else {
    return rawdate.slice(0, 10);
  }
};

export const explicitDate = (rawdate) => {
  var rawyear = rawdate.slice(0, 4);
  var rawmonth = rawdate.slice(5, 7);
  var rawday = rawdate.slice(8, 10);
  var month = parseInt(rawmonth, 10);
  var day = parseInt(rawday, 10);
  return rawyear + " 年 " + month + " 月 " + day + " 日";
};

export const difDate = (rawdate) => {
  var date = new Date();
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  year = parseInt(year, 10);
  month = parseInt(month, 10);
  day = parseInt(day, 10);
  var rawyear = rawdate.slice(0, 4);
  var rawmonth = rawdate.slice(5, 7);
  var rawday = rawdate.slice(8, 10);
  rawyear = parseInt(rawyear, 10);
  rawmonth = parseInt(rawmonth, 10);
  rawday = parseInt(rawday, 10);
  let difyear = (year - rawyear)*365;
  let difmonth = (month - rawmonth)*30;
  let difday = day - rawday;
  return difyear + difmonth + difday;
};

export const getTime = (rawdate) =>{
  let time = rawdate.slice(11, 16);
  return time;
}
