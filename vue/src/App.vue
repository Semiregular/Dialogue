<template>
  <router-view />
</template>

<script setup>
//获取窗口信息
import { useWindowStore } from "./stores";
import { useUserStore } from "./stores/user";
import { getCookie, removeCookie, setCookie } from "./utils/cookies";
import { ElNotification } from "element-plus";
import { ref, getCurrentInstance } from "vue";
let url;
let width;
let height;
let type = 1;
const windowStore = useWindowStore();
const userStore = useUserStore();
const { proxy } = getCurrentInstance();

let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);

width = window.innerWidth;
height = window.innerHeight;

if (width <= 600) {
  type = 2;
}

windowStore.$patch({
  type: type,
  width: width,
  height: height,
});

//监听窗口大小改变
window.onresize = () => {
  type = 1;
  width = window.innerWidth;
  height = window.innerHeight;
  if (width <= 600) {
    type = 2;
  }
  windowStore.$patch({
    type: type,
    width: width,
    height: height,
  });
};

url = `/user/get/ip`;
proxy.$axios
  .get(url)
  .then((response) => {
    if (response.status === 200 && response.data.code === 1) {
      userStore.$patch({
        ip: response.data.data,
      });
      if (idxUser.value) {
        url = `/user/update/ip?id=${idxUser.value}&ip=${response.data.data}`;
        proxy.$axios
          .get(url)
          .then((res) => {})
          .catch((error) => {});
      }
    }
  })
  .catch((error) => {});
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
