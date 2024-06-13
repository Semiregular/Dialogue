<template>
  <div class="main">
    <Header></Header>
    <div class="search">
      <input
        v-model="key"
        placeholder="请输入搜索内容"
        @keyup.enter="openSearch()"
      />
      <div class="icon" @click="openSearch()">
        <el-icon><Search /></el-icon>
      </div>
    </div>
    <div class="article-list">
      <ArticleList
        v-for="item in article"
        class="article-list-item"
        :title="item.title"
        :subtitle="item.subtitle"
        :userName="item.userName"
        :summary="item.summary"
        :articleView="item.articleView"
        :articleLike="item.articleLike"
        :articlePhotoUrl="item.articlePhotoUrl"
        :gmtCreate="item.gmtCreate"
        @click="read(item.id)"
      ></ArticleList>
    </div>
  </div>
</template>
<script setup>
import Header from "@/components-2/Header.vue";
import ArticleList from "@/components-2/ArticleList.vue";
import { ElNotification } from "element-plus";
import dynamics from "dynamics.js";
import { ref, getCurrentInstance, onMounted } from "vue";
const { proxy } = getCurrentInstance();
let url = "";
let article = ref([]);
let key = ref();

const openSearch = () => {
  if (key.value) {
    let searchRoute = proxy.$router.resolve({
      name: "search",
      query: {
        key: encodeURIComponent(key.value),
        orderType: 1,
        pageCurrent: 1,
        pageSize: 10,
      },
    });
    window.open(searchRoute.href, "_self");
  } else {
    ElMessage({
      showClose: false,
      message: "搜索内容不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const read = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: id,
    },
  });
  window.open(readRoute.href, "_self");
};

onMounted(() => {
  key.value = decodeURIComponent(proxy.$route.query.key);
  url = `/article/search/mysql?key=${key.value}&pageCurrent=1&pageSize=20&orderType=0`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        article.value = response.data.data.record;
        setTimeout(() => {
          let items = document.getElementsByClassName("article-list-item");
          for (let i = 0; i < items.length; i++) {
            dynamics.animate(
              items[i],
              {
                translateX: 100,
                opacity: 1,
              },
              {
                type: dynamics.easeInOut,
                duration: 500,
                friction: 500,
                delay: i * 150,
              }
            );
          }
        }, 300);
      }
    })
    .catch((error) => {

    });
});
</script>
<style scoped>
.main {
  width: 100vw;
  height: 100vh;
  overflow-x: hidden;
}
.main .search {
  width: 90%;
  margin-left: 5%;
  height: 4vh;
  margin-top: 2vh;
  margin-bottom: 2vh;
  display: flex;
}
.search input {
  width: 100%;
  height: 100%;
  text-indent: 5px;
  outline: none;
  border: none;
  border-radius: 6px;
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
  border-bottom: 1px solid red;
  font-size: 18px;
}
.search .icon {
  width: 4vh;
  height: 4vh;
  font-size: 20px;
  margin-left: 10px;
  text-align: center;
  align-items: center;
  display: flex;
  justify-content: center;
  flex-direction: column;
}
.main .article-list {
  margin-left: 5%;
  margin-top: 3vh;
  width: 90%;
  height: auto;
}
</style>
