<template>
  <div class="main-1" v-if="type === 1">
    <User_1></User_1>
  </div>
  <div class="main-2" v-if="type === 2">
    <User_2></User_2>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import User_1 from "@/views-1/User.vue";
import User_2 from "@/views-2/User.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
