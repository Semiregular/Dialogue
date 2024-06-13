<template>
  <div class="model-list-item-main">
    <div class="user" v-if="role === 'user'">
      <div class="photo">
        <img src="../assets/pic-8.jpg" class="image" />
      </div>
      <div class="text"><p>{{ content }}</p></div>
    </div>
    <div class="assistant" v-if="role === 'assistant'">
      <div class="photo">
        <img src="../assets/pic-8.jpg" class="image" />
      </div>
      <div class="text"><p>{{ content }}</p></div>
    </div>
  </div>
</template>
<script setup>
const props = defineProps(["role", "content"]);
</script>
<style scoped>
.model-list-item-main {
  margin-left: 20px;
  margin-top: 20px;
  width: calc(100% - 40px);
}
.user {
  width: 100%;
  display: flex;
  flex-direction: row-reverse;
}
.photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  user-select: none;
}
.image{
  width:100%;
  height:100%;
  border-radius: 50%;
}
.text {
  max-width:65%;
  font-size: 20px;
  border-radius: 5px;
  padding-left: 8px;
  padding-right: 8px;
  padding-top: 8px;
  padding-bottom: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}
.user .text {
  text-align: left;
  margin-right: 10px;
  color:white;
  background: conic-gradient(
    from 120deg at 50% 50%,
    #790e09,
    #961a09,
    #b32b09,
    #d03f09,
    #ef5807,
    #fa7819,
    #fd9833,
    #ffb44e
  );
}
.assistant {
  width: 100%;
  display: flex;
  flex-direction: row;
}
.assistant .text {
  text-align: left;
  margin-left: 10px;
  border: 1px solid rgba(0,0,0,0.15);
}
.text p{
  white-space: pre-wrap; 
  word-wrap: break-word;
}
</style>
