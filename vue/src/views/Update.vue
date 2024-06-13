<template>
  <div class="main-1" v-if="type === 1">
    <Update_1></Update_1>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Update_1 from "@/views-1/Update.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
