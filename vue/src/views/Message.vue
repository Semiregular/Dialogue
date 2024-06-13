<template>
  <div class="main-1" v-if="type === 1">
    <Message_1></Message_1>
  </div>
  <div class="main-2" v-if="type === 2">
    <Message_2></Message_2>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Message_1 from "@/views-1/Message.vue";
import Message_2 from "@/views-2/Message.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
