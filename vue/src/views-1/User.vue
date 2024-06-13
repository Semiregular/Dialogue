<template>
  <div class="user-main">
    <Header></Header>
    <div
      class="draggable"
      @mousedown="startDrag"
      @touchstart="startDrag"
      @mousemove="onDrag"
      @touchmove="onDrag"
      @mouseup="stopDrag"
      @touchend="stopDrag"
      @mouseleave="stopDrag"
    >
      <svg class="bg" width="1400" height="560">
        <defs>
          <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#1df" />
            <stop offset="100%" stop-color="#faf" />
          </linearGradient>
        </defs>
        <path :d="headerPath" fill="url(#gradient)"></path>
      </svg>
      <div class="content" :style="contentPosition">
        <div class="left">
          <div class="photo">
            <img :src="idxPhotoUrl" class="image" v-if="idxPhoto !== 0" />
            <img
              src="../assets/pic-8.jpg"
              class="image"
              v-if="idxPhoto === 0"
            />
          </div>
          <div class="user-text">
            <div class="name">{{ userName }}</div>
            <div class="intro">{{ userIntro }}</div>
          </div>
        </div>
        <div class="right">
          <div class="item">
            <div class="data">{{ totalArticleView }}</div>
            <div class="text">总浏览量</div>
          </div>
          <div class="item">
            <div class="data">{{ totalFollowerNum }}</div>
            <div class="text">粉丝数</div>
          </div>
          <div class="item" id="create">
            <div class="data">{{ create }}</div>
            <div class="text">加入时间</div>
          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="core">
        <div class="type">
          <div
            :class="{ 'type-item': true, 'type-active': type === 0 }"
            @click="changeType(0)"
            @mouseenter="changeHoverType(0)"
            @mouseleave="returnType()"
          >
            我的投稿
          </div>
          <div
            :class="{ 'type-item': true, 'type-active': type === 1 }"
            @click="changeType(1)"
            @mouseenter="changeHoverType(1)"
            @mouseleave="returnType()"
          >
            我的收藏
          </div>
          <div
            :class="{ 'type-item': true, 'type-active': type === 2 }"
            @click="changeType(2)"
            @mouseenter="changeHoverType(2)"
            @mouseleave="returnType()"
          >
            我的历史
          </div>
          <div
            :class="{ 'type-item': true, 'type-active': type === 3 }"
            @click="changeType(3)"
            @mouseenter="changeHoverType(3)"
            @mouseleave="returnType()"
          >
            设置
          </div>
        </div>

        <div class="edit" v-show="clickType === 0">
          <div class="loading" v-if="isload">
            <el-skeleton style="width: 850px" animated>
              <template #template>
                <div style="display: flex; width: 100%; margin-top: 3vh">
                  <el-skeleton-item
                    variant="image"
                    style="
                      width: 180px;
                      height: 180px;
                      margin-right: 20px;
                      border-radius: 8px;
                    "
                  />
                  <div>
                    <el-skeleton-item variant="h3" style="width: 650px" />
                    <el-skeleton-item variant="text" style="width: 30%" />
                    <el-skeleton-item variant="text" style="margin-top: 5vh" />
                    <el-skeleton-item variant="text" style="width: 100%" />
                    <el-skeleton-item variant="text" style="width: 80%" />
                    <el-skeleton-item variant="text" style="width: 100%" />
                    <el-skeleton-item variant="text" style="width: 80%" />
                    <el-skeleton-item variant="text" style="width: 60%" />
                  </div>
                </div>
              </template>
            </el-skeleton>
            <el-skeleton style="width: 850px" animated>
              <template #template>
                <div style="display: flex; width: 100%; margin-top: 3vh">
                  <el-skeleton-item
                    variant="image"
                    style="
                      width: 180px;
                      height: 180px;
                      margin-right: 20px;
                      border-radius: 8px;
                    "
                  />
                  <div>
                    <el-skeleton-item variant="h3" style="width: 650px" />
                    <el-skeleton-item variant="text" style="width: 30%" />
                    <el-skeleton-item variant="text" style="margin-top: 5vh" />
                    <el-skeleton-item variant="text" style="width: 100%" />
                    <el-skeleton-item variant="text" style="width: 80%" />
                    <el-skeleton-item variant="text" style="width: 100%" />
                    <el-skeleton-item variant="text" style="width: 80%" />
                    <el-skeleton-item variant="text" style="width: 60%" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
          <div class="edit-list" v-if="!iszero && !isload">
            <EditList
              v-for="item in article"
              class="edit-list-item"
              :id="item.id"
              :idxUser="item.idxUser"
              :title="item.title"
              :subtitle="item.subtitle"
              :userName="item.userName"
              :summary="item.summary"
              :articleView="item.articleView"
              :articleLike="item.articleLike"
              :articleCollect="item.articleCollect"
              :articlePhotoUrl="item.articlePhotoUrl"
              :userPhotoUrl="item.userPhotoUrl"
              :gmtCreate="item.gmtCreate"
            ></EditList>
          </div>
          <div class="no-article-list" v-if="iszero && !isload">
            开始你的创作之旅！ (*^▽^*)
          </div>
          <div class="page" v-if="!iszero && !isload">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="totalPage"
              v-model:current-page="pageCurrent"
              :page-size="pageSize"
              @update:current-page="getArticle()"
            />
          </div>
        </div>

        <div class="collect" v-show="clickType === 1">
          <div class="loading" v-if="isload">
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
          </div>
          <div class="collect-list" v-if="!iszero && !isload">
            <div v-for="item in collect" class="collect-list-item">
              <div class="title" @click="openCollect(item.idxArticle)">
                {{ item.title }}
              </div>
              <div class="time" v-html="relativeDate(item.gmtCreate)"></div>
              <div
                class="operation"
                @click="deleteCollectConfirm(item.idxArticle)"
              >
                取消收藏
              </div>
            </div>
          </div>
          <div class="no-collect-list" v-if="iszero && !isload">
            寻找更多精彩！(*^▽^*)
          </div>
          <div class="page" v-if="!iszero && !isload">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="totalPage"
              v-model:current-page="pageCurrent"
              :page-size="pageSize"
              @update:current-page="getCollect()"
            />
          </div>
        </div>

        <div class="history" v-show="clickType === 2">
          <div class="loading" v-if="isload">
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
            <el-skeleton style="width: 850px; height: 20vh" animated>
              <el-skeleton-item variant="text" style="width: 700px" />
            </el-skeleton>
          </div>
          <div class="history-list" v-if="!iszero && !isload">
            <div class="history-list-item" v-for="(item, index) in history">
              <div class="newdate" v-if="anotherday[index] === 1">
                <div class="timeline">
                  <div class="top"></div>
                  <div class="middle"></div>
                  <div class="bottom"></div>
                </div>
                <div
                  class="timenote"
                  v-html="explicitDate(item.gmtModify)"
                ></div>
              </div>

              <div class="normal">
                <div class="timeline">
                  <div class="top"></div>
                  <div class="middle"></div>
                  <div class="bottom"></div>
                </div>

                <div class="article">
                  <div
                    class="article-title"
                    @click="openHistory(item.idxArticle)"
                  >
                    {{ item.title }}
                  </div>
                  <div
                    class="article-time"
                    v-html="getTime(item.gmtModify)"
                  ></div>
                  <div class="button" @click="deleteHistory(item.id)">
                    删除历史
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="no-history-list" v-if="iszero && !isload">
            去逛逛吧！(*^▽^*)
          </div>
          <div class="page" v-if="!iszero && !isload">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="totalPage"
              v-model:current-page="pageCurrent"
              :page-size="pageSize"
              @update:current-page="getHistory()"
            />
          </div>
        </div>

        <div class="other" v-show="clickType === 3">
          <div class="other-left">
            <div
              :class="{ 'left-item': true, active: otherType === 0 }"
              @click="changeOtherType(0)"
            >
              修改用户名
            </div>
            <div
              :class="{ 'left-item': true, active: otherType === 1 }"
              @click="changeOtherType(1)"
            >
              修改密码
            </div>
            <div
              :class="{ 'left-item': true, active: otherType === 2 }"
              @click="changeOtherType(2)"
            >
              修改个人信息
            </div>
            <div
              :class="{ 'left-item': true, active: otherType === 3 }"
              @click="changeOtherType(3)"
            >
              修改用户头像
            </div>
          </div>
          <div class="other-right" v-show="otherType === 0">
            <div class="note">修改用户名</div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="用户名"
                v-model="newname"
              />
              <label class="label" id="label-1">用户名</label>
            </div>
            <div class="button" @click="updateUserName()">提交</div>
          </div>
          <div class="other-right" v-show="otherType === 1">
            <div class="note">修改密码</div>
            <div class="input" v-if="!isOringin">
              <input
                type="password"
                class="input-item"
                placeholder="原密码"
                v-model="origin"
              />
              <label class="label" id="label-1">原密码</label>
            </div>
            <div class="input" v-if="isOringin">
              <input
                type="password"
                class="input-item"
                placeholder="新密码"
                v-model="newpassword1"
              />
              <label class="label" id="label-1">新密码</label>
            </div>
            <div class="input" v-if="isOringin">
              <input
                type="password"
                class="input-item"
                placeholder="重复新密码"
                v-model="newpassword2"
              />
              <label class="label" id="label-2">重复新密码</label>
            </div>
            <div class="button" v-if="!isOringin" @click="changeOringin()">
              确定
            </div>
            <div class="button" v-if="isOringin" @click="updateUserPassword()">
              提交
            </div>
          </div>
          <div class="other-right" v-show="otherType === 2">
            <div class="note">修改个人信息</div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="手机"
                v-model="newtel"
              />
              <label class="label" id="label-1">手机</label>
            </div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="邮箱"
                v-model="newemail"
              />
              <label class="label" id="label-2">邮箱</label>
            </div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="城市"
                v-model="newcity"
              />
              <label class="label" id="label-3">城市</label>
            </div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="个人简介"
                v-model="newintro"
              />
              <label class="label" id="label-4">个人简介</label>
            </div>
            <div class="button" @click="updateUserInfo()">修改</div>
          </div>
          <div class="other-right" v-show="otherType === 3">
            <div class="note">个人头像</div>
            <div class="photo">
              <img
                :src="idxPhotoUrl"
                class="file-image"
                v-if="idxPhoto !== 0 && newIdxPhoto === 0"
              />
              <img
                src="../assets/pic-8.jpg"
                class="file-image"
                v-if="idxPhoto === 0 && newIdxPhoto === 0"
              />
              <img
                :src="newimgurl"
                class="file-image"
                v-if="newIdxPhoto !== 0"
              />
              <div
                class="file-button"
                @click="selectFile()"
                @drop="dropFile($event)"
                @dragover="dragOverFile($event)"
              >
                <div class="icon">
                  <el-icon><Plus /></el-icon>
                </div>
                <div class="imp">拖拽图片上传或<span> 点击此处</span></div>
              </div>
              <div class="imp-bottom">支持png/jpg格式，大小不超过5MB</div>
              <input
                type="file"
                name="file"
                accept="image/*"
                @change="getFile"
                ref="inputer"
                class="file-input"
              />
              <div class="button" @click="updateUserPhoto()">确定</div>
            </div>
          </div>
        </div>
      </div>
      <div class="side">
        <div class="item">
          <div class="text">IP属地：</div>
          <div class="text" v-for="item in ip">{{ item }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Header from "@/components-1/Header.vue";
import EditList from "@/components-1/EditList.vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "../stores/user.js";
import { ref, reactive, getCurrentInstance, onMounted, computed } from "vue";
import { getCookie, setCookie } from "@/utils/cookies";
import dynamics from "dynamics.js";
import { ElNotification, ElMessage, ElMessageBox } from "element-plus";
const { proxy } = getCurrentInstance();
import { SHA256 } from "@/utils/SHA256";
import {
  explicitDate,
  getDate,
  difDate,
  getTime,
  relativeDate,
} from "@/utils/getDate";
const userStore = useUserStore();
const { ip } = storeToRefs(userStore);

let isOringin = ref(false);
let newname = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);
let idxPhotoUrl = ref(
  getCookie("idxPhotoUrl").idxPhotoUrl
    ? getCookie("idxPhotoUrl").idxPhotoUrl
    : 0
);
let otherType = ref(0);
let pageCurrent = ref(1);
let totalPage = ref(1);
let pageSize = ref(20);
let isload = ref(true);
let iszero = ref(true);
let newIdxPhoto = ref(0);
let newimgurl = ref();
let diftime = [];
let file;
let anotherday = ref([]);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
let idxPhoto = ref(
  Number(getCookie("idxPhoto").idxPhoto)
    ? Number(getCookie("idxPhoto").idxPhoto)
    : 0
);
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);
let url;
let newcity = ref();
let newintro = ref();
let newtel = ref();
let newemail = ref();
let userCity = ref();
let userLevel = ref();
let userIntro = ref();
let gmtCreate = ref();
let create = ref();
let totalArticleView = ref();
let totalArticleLike = ref();
let totalArticleNum = ref();
let totalArticleCollect = ref();
let totalFollowerNum = ref();
let blob;
let imgurl = ref();
let article = ref([]);
let collect = ref([]);
let history = ref([]);
let type = ref(0);
let clickType = ref(0);
let newpassword1 = ref();
let newpassword2 = ref();
let origin = ref();

const changeType = (num) => {
  clickType.value = num;
  if (num === 0) {
    getArticle();
  }
  if (num === 1) {
    getCollect();
  }
  if (num === 2) {
    getHistory();
  }
};
const changeOtherType = (num) => {
  otherType.value = num;
  isOringin.value = false;
};
const openCollect = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: id,
    },
  });
  window.open(readRoute.href, "_blank");
};

const openHistory = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: id,
    },
  });
  window.open(readRoute.href, "_blank");
};

const changeOringin = () => {
  proxy.$axios
    .post("/user/login", {
      loginType: "PASSWORD",
      userName: userName.value,
      userPassword: SHA256(origin.value),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        isOringin.value = true;
      } else {
        ElNotification({
          title: "密码错误",
          message: "失败",
          type: "error",
        });
      }
    })
    .catch((error) => {
      ElNotification({
        title: "提交失败",
        message: error,
        type: "error",
      });
    });
};

const changeHoverType = (num) => {
  type.value = num;
};

const returnType = () => {
  type.value = clickType.value;
};

const getArticle = () => {
  url = `/user/get/article?idxUser=${idxUser.value}&pageCurrent=1&pageSize=20`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        article.value = response.data.data.record;
        totalPage.value = response.data.data.total;
        if (article.value.length) {
          iszero.value = false;
          setTimeout(() => {
            isload.value = false;
          }, 300);
        } else {
          setTimeout(() => {
            isload.value = false;
          }, 300);
          iszero.value = true;
        }
      }
    })
    .catch((error) => {

    });
};

const updateUserPassword = () => {
  if (newpassword1.value) {
    if (newpassword1.value === newpassword2.value) {
      proxy.$axios
        .post("/user/update/password", {
          id: idxUser.value,
          userPassword: SHA256(newpassword1.value),
          gmtModify: getDate(),
        })
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            ElMessage({
              showClose: false,
              message: "修改成功",
              type: "success",
              duration: 1000,
              center: true,
              grouping: false,
            });
          }
        })
        .catch((error) => {
          ElNotification({
            title: "提交失败",
            message: error,
            type: "error",
          });
        });
    } else {
      ElMessage({
        showClose: false,
        message: "两次密码输入不一致",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: false,
      });
    }
  } else {
    ElMessage({
      showClose: false,
      message: "密码不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const updateUserName = () => {
  if (newname.value) {
    proxy.$axios
      .post("/user/update/username", {
        id: idxUser.value,
        userName: newname.value,
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          setCookie("userName", newname.value);
          userName.value = newname.value;
          ElMessage({
            showClose: false,
            message: "修改成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
        }
      })
      .catch((error) => {
        ElNotification({
          title: "提交失败",
          message: error,
          type: "error",
        });
      });
  } else {
    ElMessage({
      showClose: false,
      message: "用户名不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const updateUserPhoto = () => {
  if (newIdxPhoto.value) {
    proxy.$axios
      .post("/user/update/photo", {
        id: idxUser.value,
        userPhoto: newIdxPhoto.value,
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          setCookie("idxPhoto", newIdxPhoto.value);
          setCookie("idxPhotoUrl", newimgurl.value);
          idxPhoto.value = newIdxPhoto.value;
          idxPhotoUrl.value = newimgurl.value;
          ElMessage({
            showClose: false,
            message: "修改成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
        }
      })
      .catch((error) => {
        ElNotification({
          title: "提交失败",
          message: error,
          type: "error",
        });
      });
  } else {
    ElMessage({
      showClose: false,
      message: "图像不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const updateUserInfo = () => {
  proxy.$axios
    .post("/user/update/info", {
      id: idxUser.value,
      userTel: newtel.value,
      userEmail: newemail.value,
      userCity: newcity.value,
      userIntro: newintro.value,
      gmtModify: getDate(),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        ElMessage({
          showClose: false,
          message: "修改成功",
          type: "success",
          duration: 1000,
          center: true,
          grouping: false,
        });
        location.reload();
      }
    })
    .catch((error) => {});
};

const getCollect = () => {
  url = `/user/get/collect?idxUser=${idxUser.value}&pageCurrent=1&pageSize=20`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        collect.value = response.data.data.records;
        totalPage.value = response.data.data.pages;
        if (collect.value.length) {
          iszero.value = false;
          setTimeout(() => {
            isload.value = false;
          }, 300);
        } else {
          setTimeout(() => {
            isload.value = false;
          }, 300);
          iszero.value = true;
        }
      }
    })
    .catch((error) => {});
};

const deleteCollectConfirm = (idxArticle) => {
  ElMessageBox.confirm("此操作将取消收藏，确定要继续吗?", "Warning", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteCollect(idxArticle);
    })
    .catch(() => {});
};

const deleteCollect = (idxArticle) => {
  updateHistory(idxArticle);
  url = `/user/update/collect?idxUser=${idxUser.value}&idxArticle=${idxArticle}`;
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
          grouping: false,
        });
        getCollect();
      }
    })
    .catch((error) => {});
};

const deleteHistory = (id) => {
  url = `/user/delete/history?id=${id}`;
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
          grouping: false,
        });
        getHistory();
      }
    })
    .catch((error) => {});
};

const getHistory = () => {
  url = `/user/list/history?idxUser=${idxUser.value}&pageCurrent=1&pageSize=10`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        history.value = response.data.data.records;
        totalPage.value = response.data.data.pages;
        if (history.value.length) {
          for (let i = 0; i < history.value.length; i++) {
            let tmp = history.value[i];
            diftime.push(difDate(tmp.gmtModify));
          }
          if (diftime.length === 1) {
            anotherday.value = 1;
          } else {
            anotherday.value.push(1);
            for (let i = 1; i < diftime.length; i++) {
              if (diftime[i] === diftime[i - 1]) {
                anotherday.value.push(0);
              } else {
                anotherday.value.push(1);
              }
            }
          }
          iszero.value = false;
          setTimeout(() => {
            isload.value = false;
          }, 300);
        } else {
          setTimeout(() => {
            isload.value = false;
          }, 300);
          iszero.value = true;
        }
      }
    })
    .catch((error) => {});
};

const updateHistory = (idxArticle) => {
  proxy.$axios
    .post("/user/update/history/collect", {
      idxUser: idxUser.value,
      idxArticle: idxArticle,
      isCollect: 0,
      gmtModify: getDate(),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {
      ElNotification({
        title: "更新失败",
        message: error,
        type: "error",
        duration: 2000,
      });
    });
};

const selectFile = () => {
  let fileInput = document.getElementsByClassName("file-input")[0];
  fileInput.click();
};

const dropFile = (e) => {
  e.preventDefault();
  file = e.dataTransfer.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/user", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            newIdxPhoto.value = response.data.data.id;
            newimgurl.value=response.data.data.url;
          }
        })
        .catch((error) => {
        });
    }
  }
};

const dragOverFile = (e) => {
  e.preventDefault();
};

const getFile = () => {
  let inputDOM = proxy.$refs.inputer;
  file = inputDOM.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/user", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            newIdxPhoto.value = response.data.data.id;
            newimgurl.value=response.data.data.url;
          }
        })
        .catch((error) => {
        });
    }
  }
};

onMounted(() => {
  url = `/user/get/info?idxUser=${idxUser.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        userCity.value = response.data.data.userCity;
        newcity.value = response.data.data.userCity;
        newtel.value = response.data.data.userTel;
        newemail.value = response.data.data.userEmail;
        userLevel.value = response.data.data.userLevel;
        userIntro.value = response.data.data.userIntro;
        newintro.value = response.data.data.userIntro;
        gmtCreate.value = response.data.data.gmtCreate;
        totalArticleCollect.value = response.data.data.totalArticleCollect;
        totalArticleView.value = response.data.data.totalArticleView;
        totalArticleLike.value = response.data.data.totalArticleLike;
        totalArticleNum.value = response.data.data.totalArticleNum;
        totalFollowerNum.value = response.data.data.totalFollowerNum;
        create.value = explicitDate(gmtCreate.value);
      }
    })
    .catch((error) => {});

  url = `/user/get/article?idxUser=${idxUser.value}&pageCurrent=1&pageSize=10`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        article.value = response.data.data.record;
        totalPage.value = response.data.data.total;
        if (article.value.length) {
          iszero.value = false;
          setTimeout(() => {
            isload.value = false;
          }, 300);
        } else {
          setTimeout(() => {
            isload.value = false;
          }, 300);
          iszero.value = true;
        }
      }
    })
    .catch((error) => {});
});

const headerHeight = 180;

let isDragging = false;
const start = { x: 0, y: 0 };
const c = reactive({ x: headerHeight, y: headerHeight });

const headerPath = computed(() => {
  return `M0,0 L1400,0 1400,${headerHeight}Q${c.x},${c.y} 0,${headerHeight}`;
});

const contentPosition = computed(() => {
  const dy = c.y - headerHeight;
  const dampen = dy > 0 ? 2 : 4;
  return {
    transform: `translate(0,${dy / dampen}px)`,
  };
});

function startDrag(e) {
  e = e.changedTouches ? e.changedTouches[0] : e;
  isDragging = true;
  start.x = e.pageX;
  start.y = e.pageY;
}

function onDrag(e) {
  e = e.changedTouches ? e.changedTouches[0] : e;
  if (isDragging) {
    c.x = headerHeight + (e.pageX - start.x);
    const dy = e.pageY - start.y;
    const dampen = dy > 0 ? 1.5 : 4;
    c.y = headerHeight + dy / dampen;
  }
}

function stopDrag() {
  if (isDragging) {
    isDragging = false;
    dynamics.animate(
      c,
      { x: headerHeight, y: headerHeight },
      { type: dynamics.spring, duration: 700, friction: 280 }
    );
  }
}
</script>
<style scoped>
.user-main {
  width: 100%;
  min-height: 100vh;
}
.draggable {
  background-color: #fff;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  margin-left: 10%;
  width: 80%;
  height: 300px;
  overflow: hidden;
  position: relative;
  text-align: center;
  font-size: 14px;
  font-weight: 300;
  user-select: none;
}
.container {
  width: 80%;
  min-height: 40vh;
  height: auto;
  margin-top: 3vh;
  margin-left: 10%;
  background-color: #fff;
  box-shadow: 0 0px 16px rgba(0, 0, 0, 0.15);
  display: flex;
}
.bg {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}
.draggable .content {
  position: relative;
  display: flex;
  justify-content: space-around;
  z-index: 1;
  margin-top: 180px;
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
}
.content .left {
  display: flex;
  width: 60%;
}
.content .left .photo {
  width: 6vh;
  height: 6vh;
  margin-right: 20px;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.user-text {
  width: 80%;
}
.user-text .intro {
  width: 100%;
  height: 3vh;
  text-align: left;
  overflow: hidden;
  margin-top: 5px;
}
.draggable .content .left .name {
  text-align: left;
  font-size: 25px;
  font-weight: 600;
  margin-left: 1px;
  color: rgb(214, 31, 31);
}
.content .right {
  min-width: 200px;
  display: flex;
}
.content .right .item {
  width: 25%;
  min-width: 60px;
  text-align: center;
  margin-right: 20px;
  overflow: hidden;
}
.content .right .item .data {
  height: 3vh;
  line-height: 3vh;
  overflow: hidden;
}
.content .right .item .text {
  margin-top: 3px;
  min-width: 70px;
}
#create {
  width: 45%;
  min-width: 110px;
}
.container .core {
  width: 70%;
  margin-top: 3vh;
  margin-bottom: 3vh;
  margin-left: 3%;
}
.core .type {
  display: flex;
  height: 4vh;
  font-size: 18px;
  font-weight: 500;
  width:fit-content;
}

.type .type-item {
  text-align: center;
  min-width: 100px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
  user-select: none;
}
.type .type-item:hover {
  color: rgb(214, 31, 31);
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
}
.type .type-active {
  color: rgb(214, 31, 31);
  border-bottom: 2px solid rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.core .edit-list {
  margin-top: 3vh;
}
.edit-list {
  transition: all 0.5s ease;
}
.edit-list-item:hover {
  transition: all 0.5s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transform: translateX(20px);
}
.core .collect-list {
  margin-top: 3vh;
  margin-bottom: 3vh;
}
.core .collect-list-item {
  width: 100%;
  height: 6vh;
  margin-bottom: 2vh;
  display: flex;
  border: 1px solid #eee;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.collect-list-item .title {
  display: flex;
  width: 60%;
  min-width: 150px;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  font-weight: 500;
  margin-left: 10px;
  font-size: 18px;
  cursor: pointer;
  user-select: none;
}
.collect-list-item .time {
  display: flex;
  margin-left: 20px;
  min-width: 70px;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  font-weight: 500;
  overflow: hidden;
  user-select: none;
}
.collect-list-item .operation {
  display: flex;
  margin-left: 10px;
  margin-right: 20px;
  min-width: 100px;
  height: 3vh;
  overflow: hidden;
  text-align: center;
  margin-top: 1.5vh;
  user-select: none;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
  font-weight: 500;
  color: white;
  background-color: rgb(214, 31, 31);
  border-radius: 6px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.collect-list-item:hover {
  transform: translateX(10px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-color: #fde2e2;
  transition: all 0.2s ease;
}
.container .side {
  width: 21%;
  margin-left: 3%;
  margin-top: 3vh;
  margin-bottom: 3vh;
}
.core .loading {
  width: 100%;
  height: 55vh;
  overflow: hidden;
}
:deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: rgb(214, 31, 31);
}
.side .item {
  color: rgba(0, 0, 0, 0.5);
  user-select: none;
  font-size: 14px;
  height: 3vh;
  line-height: 3vh;
  display: flex;
}
.side .item .text {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 50px;
}
.no-collect-list {
  width: 100%;
  height: 30vh;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.5);
  user-select: none;
}
.no-article-list {
  width: 100%;
  height: 30vh;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.5);
  user-select: none;
}
.no-history-list {
  width: 100%;
  height: 30vh;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.5);
  user-select: none;
}
.other {
  display: flex;
  width: 100%;
  margin-top: 3vh;
  min-height: 40vh;
  height: fit-content;
}
.other .other-left {
  min-width: 130px;
}
.other-left .left-item {
  width: 100%;
  user-select: none;
  height: 5vh;
  font-size: 16px;
  padding-left: 10px;
  margin-bottom: 1.5vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-left: 2px solid transparent;
  transition: all 0.3s ease;
}
.other-left .left-item:hover {
  background-color: rgba(214, 31, 31, 0.1);
  border-left: 2px solid rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}
.other-left .active {
  background-color: rgba(214, 31, 31, 0.1);
  border-left: 2px solid rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}
.other .other-right {
  width: 90%;
  margin-left: 50px;
}
.other .note {
  font-size: 20px;
  font-weight: 500;
  width: 100%;
  height: 3vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 1vh;
  margin-bottom: 3vh;
  user-select: none;
}
.input-item {
  outline: none;
  height: 5vh;
  line-height: 34px;
  box-sizing: border-box;
  font-size: 16px;
  font-weight: 500;
  padding-top: 0.6vh;
  padding-left: 1vh;
  border: 1px solid #ccc;
  border-radius: 4px;
  color: black;
  width: 100%;
  text-indent: 5px;
}

.input-item:not(:placeholder-shown),
.input-item:focus {
  border-color: rgb(214, 31, 31);
}

.input-item::placeholder {
  color: transparent;
}
.input-item:not(:placeholder-shown) ~ .label,
.input-item:focus ~ .label {
  transform: scale(0.75) translate(0, -3vh);
  background: #fff;
  color: rgb(214, 31, 31);
}
.label {
  position: absolute;
  transition: transform 0.25s;
  font-size: 16px;
  padding: 0 6px;
  pointer-events: none;
  color: #999;
}
#label-1 {
  left: calc(12.5vw + 180px);
  top: calc(29vh + 300px);
}
#label-2 {
  left: calc(12.5vw + 180px);
  top: calc(36vh + 300px);
}
#label-3 {
  left: calc(12.5vw + 180px);
  top: calc(43vh + 300px);
}
#label-4 {
  left: calc(12.5vw + 180px);
  top: calc(50vh + 300px);
}
.other .input {
  margin-bottom: 2vh;
}
.other-right .button {
  width: 100px;
  background-color: rgb(214, 31, 31);
  color: white;
  text-align: center;
  margin-top: 2vh;
  margin-left: calc(100% - 100px);
  font-size: 16px;
  cursor: pointer;
  user-select: none;
  border-radius: 6px;
  height: 4vh;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.history-list {
  width: 100%;
  height: auto;
  margin-top: 3vh;
  margin-left: 2vh;
  margin-bottom: 3vh;
}
.history-list-item {
  height: auto;
}
.history-list-item .newdate {
  display: flex;
  width: 100%;
  height: calc(4vh + 20px);
}
.history-list-item .normal {
  display: flex;
  width: 100%;
  height: calc(8vh + 15px);
}
.newdate .timeline {
  height: calc(4vh + 20px);
}
.newdate .timeline .top {
  margin-left: 7px;
  margin-right: 7px;
  width: 5px;
  height: 2vh;
  background-color: rgb(214, 31, 31);
}
.history-list-item:nth-child(1) .newdate .timeline .top {
  background-color: white;
}
.history-list-item:nth-last-child(1) .normal .timeline .bottom {
  background-color: white;
}
.newdate .timeline .middle {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  border: 3px solid #00bfff;
  position: relative;
}

.newdate .timeline .bottom {
  margin-left: 7px;
  margin-right: 7px;
  width: 5px;
  height: 2vh;
  background-color: rgb(214, 31, 31);
}
.timenote {
  margin: auto;
  margin-left: 20px;
  font-size: 16px;
  user-select: none;
}
.normal .timeline .top {
  margin-left: 7px;
  margin-right: 7px;
  width: 5px;
  height: 4vh;
  background-color: rgb(214, 31, 31);
}
.normal .timeline .middle {
  width: 15px;
  height: 15px;
  margin-left: 1px;
  border-radius: 50%;
  border: 2px solid rgb(214, 31, 31);
  position: relative;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.normal:hover .timeline .middle {
  border-color: #ff8c00;
}
.normal:hover .timeline .middle::before {
  background-color: #ff8c00;
}
.normal .timeline .middle::before {
  width: 5px;
  height: 5px;
  top: 5px;
  position: absolute;
  left: 4.5px;
  border-radius: 50%;
  background-color: rgb(214, 31, 31);
  content: "";
  display: inline-block;
}
.normal .timeline .bottom {
  margin-left: 7px;
  margin-right: 7px;
  width: 5px;
  height: 4vh;
  background-color: rgb(214, 31, 31);
}
.history-list-item .article {
  display: flex;
  border-radius: 8px;
  width: calc(90% - 35px);
  margin: auto;
  margin-left: 20px;
  font-size: 16px;
  height: 4vh;
  padding-top: 1vh;
  padding-bottom: 1vh;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid #eee;
  transition: all 0.2s ease;
  user-select: none;
}
.history-list-item .article:hover {
  transition: all 0.2s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-color: #fde2e2;
  transform: translateX(10px);
}
.history-list-item .article .article-title {
  margin-left: 15px;
  min-width: 100px;
  width: calc(100px + 50%);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.history-list-item .article .article-time {
  display: flex;
  margin-right: 20px;
  line-height: 100%;
  height: 100%;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
  min-width: 50px;
}
.history-list-item .article .button {
  cursor: pointer;
  margin-right: 20px;
  display: flex;
  min-width: 80px;
  flex-direction: column;
  justify-content: center;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  font-size: 16px;
  text-align: center;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.history {
  min-height: 30vh;
}
.collect {
  min-height: 30vh;
}

.file-input {
  outline: none;
  border: none;
  text-align: center;
  display: none;
}
.file-image {
  margin-top: 1vh;
  width: 10vh;
  height: 10vh;
  border-radius: 50%;
  margin-bottom: 3vh;
}

.file-button {
  width: 100%;
  height: 20vh;
  border-radius: 8px;
  border: 2px dashed rgba(0, 0, 0, 0.3);
  transition: all 0.2s ease;
}
.file-button:hover {
  border: 2px dashed rgb(214, 31, 31);
}
.imp {
  text-align: center;
  margin-top: 2vh;
  width: 100%;
  font-size: 14px;
}
.imp span {
  color: rgb(214, 31, 31);
}
.file-button .icon {
  width: 100%;
  height: 5vh;
  margin-top: 6vh;
  font-size: 30px;
  color: rgb(214, 31, 31);
  font-weight: 100;
  text-align: center;
}
.imp-bottom {
  text-align: center;
  font-size: 12px;
  margin-top: 1vh;
  color: rgba(0, 0, 0, 0.5);
}
</style>
