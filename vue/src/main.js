import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import axios from "axios";
import piniaPersist from "pinia-plugin-persist";
import ElementPlus from "element-plus";
import "element-plus/theme-chalk/index.css";
import locale from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { getCookie } from "./utils/cookies";


//app实例化
const app = createApp(App);

//Element图标库引入
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

//pinia状态管理持久化
const pinia = createPinia();
pinia.use(piniaPersist);

//axios
app.config.globalProperties.$axios = axios;
axios.defaults.baseURL = "/api";
axios.interceptors.request.use((config) => {
  //请求头加入token
  config.headers.token = getCookie("token").token;
  return config;
});

//解决页面拖动异常
const debounce = (fn, delay) => {
  let timer = null;
  return function () {
    let context = this;
    let args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  };
};

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
  constructor(callback) {
    callback = debounce(callback, 16);
    super(callback);
  }
};

app.use(pinia).use(router).use(ElementPlus, { locale }).mount("#app");
