<template>
  <div class="main-2" v-if="type === 2">
    <Home_2></Home_2>
  </div>
  <div class="main-1" v-if="type === 1">
    <Home_1></Home_1>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Home_1 from "@/views-1/Home.vue";
import Home_2 from "@/views-2/Home.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped>
.main-2 {
  width: 100vw;
  height: 100vh;
}
</style>
