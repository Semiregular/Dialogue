<template>
  <div class="article-item-main">
    <div class="photo">
      <img :src="articlePhotoUrl" class="image" />
    </div>
    <div class="article">
      <div class="title">
        <div class="maintitle" @click="read()">{{ title }}</div>
        <div class="subtitle">{{ subtitle }}</div>
      </div>
      <div class="user">
        <div class="user-photo">
        <img :src="userPhotoUrl" class="userimage" />
        </div>
        <div class="name">{{ userName }}</div>
      </div>
      <div class="summary" @click="read()">{{ summary }}</div>
      <div class="other">
        <div class="view">
          <div class="icon">
            <el-icon><View /></el-icon>
          </div>
          <div class="data">{{ articleView }}</div>
        </div>
        <div class="like">
          <div class="icon">
            <el-icon><ArrowUp /></el-icon>
          </div>
          <div class="data">{{ articleLike }}</div>
        </div>
        <div class="time">
          {{ create }}
        </div>
      </div>
    </div>
    <div class="operation">
      <div class="item" @click="update()">编辑</div>
      <div class="item" @click="deleteConfirm()">删除</div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, getCurrentInstance } from "vue";
import { relativeDate } from "@/utils/getDate";
import { ElNotification, ElMessage, ElMessageBox } from "element-plus";
const { proxy } = getCurrentInstance();
let url;
let create = ref();
const props = defineProps([
  "id",
  "idxUser",
  "userName",
  "userPhotoUrl",
  "title",
  "subtitle",
  "summary",
  "gmtCreate",
  "articleView",
  "articleLike",
  "articleCollect",
  "articleShare",
  "articlePhotoUrl",
]);

const deleteConfirm = () => {
  ElMessageBox.confirm("此操作将删除文章，确定要继续吗?", "Warning", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteArticle();
    })
    .catch(() => {});
};

const deleteArticle = () => {
  let id = Number(props.id);
  url = `/article/delete/mysql?id=${id}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        ElMessage({
          showClose: false,
          message: "删除成功",
          type: "success",
          duration: 1000,
          center: true,
          grouping: true,
        });
        location.reload();
      }
    })
    .catch((error) => {

    });
};

const read = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: Number(props.id),
    },
  });
  window.open(readRoute.href, "_blank");
};

const update = (id) => {
  let updateRoute = proxy.$router.resolve({
    name: "update",
    query: {
      idxArticle: Number(props.id),
    },
  });
  window.open(updateRoute.href, "_blank");
};

onMounted(() => {
  create.value = relativeDate(props.gmtCreate);
});
</script>
<style scoped>
.article-item-main {
  width: 100%;
  height: 20vh;
  opacity: 1;
  display: flex;
  border-radius: 8px;
  margin-left: 0px;
  margin-bottom: 30px;
  flex-direction: row;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
}

.article-item-main .photo {
  width: 20%;
  border-radius: 1vh;
  user-select: none;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 1vh;
}
.article-item-main .article {
  width: 65%;
  margin-left: 3%;
  display: flex;
  flex-direction: column;
}
.operation {
  width: 10%;
  margin-left: 2%;
  margin-right: 2%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.operation .item {
  width: 100%;
  margin-bottom: 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 400;
  text-align: center;
  cursor: pointer;
  user-select: none;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}
.operation .item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.operation .item:nth-child(1) {
  color: rgb(214, 31, 31);
  background-color: white;
  border: 1px solid rgb(214, 31, 31);
}
.operation .item:nth-child(2) {
  color: white;
  background-color: rgb(214, 31, 31);
}
.article-item-main .title {
  height: 15%;
  display: flex;
  overflow: hidden;
  margin-top: 1vh;
  flex-direction: row;
  font-size: 2.5vh;
  cursor: pointer;
}
.article-item-main .title .maintitle {
  font-weight: 600;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-item-main .title .subtitle {
  margin-left: 3%;
  margin-top: 0.2vh;
  font-size: 2vh;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.75);
  display: flex;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
}
.article-item-main .user {
  display: flex;
  user-select: none;
  flex-direction: row;
  margin-top: 10px;
  height: 16px;
}
.article-item-main .user .userphoto {
  max-width: 16px;
  max-height: 16px;
}
.userimage{
  width:100%;
  height: 100%;
  max-width: 16px;
  max-height: 16px;
  border-radius: 50%;
  user-select: none;
}
.article-item-main .user .name {
  font-size: 14px;
  line-height: 16px;
  margin-left: 8px;
  font-weight: 600;
  color: rgb(215, 29, 29);
}
.article-item-main .summary {
  margin-top: 2%;
  height: 28%;
  font-size: 16px;
  line-height: 25px;
  overflow: hidden;
  font-family: "仿宋";
  cursor: pointer;
}
.article-item-main .other {
  margin-top: 3%;
  user-select: none;
  height: 3vh;
  width: 100%;
  display: flex;
  flex-direction: row;
  font-size: 12px;
  font-weight: 400;
  overflow: hidden;
  color: rgba(0, 0, 0, 0.715);
}
.article-item-main .other .view {
  display: flex;
  min-width: 40px;
}
.article-item-main .other .data {
  margin-left: 0.5vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
  line-height: 100%;
}
.article-item-main .other .like {
  display: flex;
  margin-left: 5%;
  min-width: 40px;
}
.article-item-main .other .time {
  margin-left: calc(95% - 150px);
  width: 65px;
  text-align: right;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
  line-height: 100%;
}
.article-item-main .other .icon {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

</style>
