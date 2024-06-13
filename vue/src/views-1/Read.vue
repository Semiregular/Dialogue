<template>
  <div class="read-main">
    <Header></Header>

    <!-- 固定导航 -->

    <div class="fix-head">
      <div class="title">{{ title }}</div>
      <div class="tool">
        <div class="photo" @click="openUserInfo()">
          <img :src="idxPhotoUrl" class="user-image" v-if="idxPhoto !== 0" />
          <img
            src="../assets/pic-8.jpg"
            class="user-image"
            v-if="idxPhoto === 0"
          />
        </div>
        <div class="item" @click="openUserMessage()">
          <div class="icon">
            <el-badge
              :value="messageUncheck"
              :max="9"
              class="top"
              v-if="messageUncheck !== 0"
            >
              <el-icon><Message /></el-icon>
            </el-badge>
            <el-icon v-if="messageUncheck === 0"><Message /></el-icon>
          </div>
          <div class="text">消息</div>
        </div>
        <div class="item" @click="openEdit()">
          <div class="icon">
            <el-icon><Edit /></el-icon>
          </div>
          <div class="text">投稿</div>
        </div>
      </div>
    </div>

    <div class="wrapper">
      <!-- 目录 -->

      <div class="catalogue">
        <el-scrollbar height="80vh">
          <div class="top-bar">
            <div
              :class="{ note: true, 'cur-note': cataType === 1 }"
              @click="changeCataType(1)"
            >
              目录
            </div>
            <div
              :class="{ note: true, 'cur-note': cataType === 2 }"
              @click="changeCataType(2)"
            >
              合集
            </div>
          </div>
          <div
            v-for="(item, index) in cataArr"
            :class="{ 'current-cata': curId === index + 1 }"
            :id="item.class"
            v-show="cataType === 1"
            @click="scrollCata(item.href)"
          >
            {{ item.text }}
          </div>
          <div v-show="cataType === 2"></div>
        </el-scrollbar>
      </div>

      <!-- 右侧工具栏 -->

      <div :class="{ 'right-tool': true, 'tool-active': !isHide }">
        <div class="right-tool-list">
          <el-tooltip
            content="小助手"
            placement="left"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{
                'tool-list-item': true,
                'tool-item-active': tooltype === 1 && !isHide,
              }"
              @click="changeToolType(1)"
            >
              <el-icon><Search /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip
            content="语音朗诵"
            placement="left"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{
                'tool-list-item': true,
                'tool-item-active': tooltype === 2 && !isHide,
              }"
              @click="changeToolType(2)"
            >
              <el-icon><Microphone /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip
            content="评论"
            placement="left"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{
                'tool-list-item': true,
                'tool-item-active': tooltype === 3 && !isHide,
              }"
              @click="changeToolType(3)"
            >
              <el-icon><ChatDotSquare /></el-icon>
            </div>
          </el-tooltip>
        </div>
      </div>

      <!-- 展开工具栏 -->

      <div :class="{ 'tool-sp': true, 'tool-sp-hide': isHide }">
        <div class="model" v-show="tooltype === 1">
          <Model></Model>
        </div>
        <div class="voice" v-if="tooltype === 2">
          <Voice :artraw="content"></Voice>
        </div>
        <div class="comment-sp" v-show="tooltype === 3">
          <div class="note">评论</div>
          <div class="comment-sp-list">
            <CommentMark
              v-for="item in commentMark"
              :class="'markComment-id-' + item.idxSpan"
              :id="item.id"
              :idxArticle="item.idxArticle"
              :idxAuthor="item.idxUser"
              :authorName="item.userName"
              :content="item.content"
              :commentLike="item.commentLike"
              :userPhotoUrl="item.userPhotoUrl"
              :ip="item.ip"
              :sublist="item.sublist"
              :idxSpan="item.idxSpan"
              :isLike="item.isLike"
              :gmtCreate="item.gmtCreate"
            ></CommentMark>
          </div>
        </div>
      </div>

      <!-- 文章主体 -->

      <div class="middle">
        <div class="title">
          <div class="main">{{ title }}</div>
          <div class="sub">{{ subtitle }}</div>
        </div>
        <div class="info">
          <div class="item">发布时间：{{ gmtCreate }}</div>
          <div class="item">浏览量：{{ articleView }}</div>
          <div class="item">点赞量：{{ articleLike }}</div>
          <div class="item">收藏量：{{ articleCollect }}</div>
        </div>
        <div class="author">
          <div class="photo" @click="openOtherInfo()">
            <img :src="authorPhotoUrl" class="user-image" />
          </div>
          <div class="name" @click="openOtherInfo()">{{ authorName }}</div>
        </div>
        <div class="summary">摘要：{{ summary }}</div>
        <div class="article-photo">
          <img :src="articlePhotoUrl" class="image" />
        </div>
        <div
          class="content"
          ref="contentRef"
          v-if="editType === 2"
          v-html="content"
          @mouseup="selectContent()"
          @click="showCancelTip($event)"
        ></div>
        <div
          class="content"
          ref="contentRef"
          v-if="editType === 1"
          @mouseup="selectContent()"
          @click="showCancelTip($event)"
        >
          <Viewer :value="content"></Viewer>
        </div>
        <div class="tip" v-show="isTip">
          <div class="tip-item" @click="mark()">标记</div>
          <div class="tip-item">|</div>
          <div class="tip-item" @click="setCommentMark()">评论</div>
        </div>
        <div class="delete-tip" v-show="isDeleteTip" @click="cancelMark()">
          清除
        </div>
        <div class="comment-mark" v-show="isCommentMark">
          <textarea v-model="markCommentContent"></textarea>
          <div class="mark-button" @click="sendMarkComment()">发布</div>
        </div>
        <el-divider />
        <div class="other">
          <div class="tag">
            <div class="pre">标签：</div>
            <div v-for="item in tag" class="tag-item">{{ item.tag }}</div>
          </div>
          <div class="item">修改于：{{ gmtModify }}</div>
        </div>
      </div>

      <!-- 右下工具 -->

      <div class="right-side">
        <div
          :class="{ item: true, 'opt-active': isLike === 1 }"
          @click="changeLike()"
        >
          <div class="icon">
            <el-icon><ArrowUp /></el-icon>
          </div>
          <div class="data">{{ articleLike }}</div>
        </div>
        <div
          :class="{ item: true, 'opt-active': isCollect === 1 }"
          @click="changeCollect()"
        >
          <div class="icon">
            <el-icon><Star /></el-icon>
          </div>
          <div class="data">{{ articleCollect }}</div>
        </div>
        <div class="item" @click="scrollComment()">
          <div class="icon">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <div class="data">{{ comment.length }}</div>
        </div>
        <div class="item" @click="changeShare()">
          <div class="icon">
            <el-icon><Share /></el-icon>
          </div>
          <div class="data">分享</div>
        </div>
      </div>

      <!-- 评论 -->

      <div class="comment">
        <div class="comment-title">评论 {{ comment.length }}</div>
        <div class="no-comment" v-if="iszero">还没有评论 o(╥﹏╥)o</div>
        <div class="comment-list" v-if="!iszero">
          <Comment
            v-for="item in comment"
            class="comment-list-item"
            :id="item.id"
            :idxArticle="item.idxArticle"
            :idxAuthor="item.idxUser"
            :authorName="item.userName"
            :content="item.content"
            :commentLike="item.commentLike"
            :userPhotoUrl="item.userPhotoUrl"
            :sublist="item.sublist"
            :ip="item.ip"
            :isLike="item.isLike"
            :gmtCreate="item.gmtCreate"
          ></Comment>
        </div>

        <!-- 提交评论固定页面底部 -->

        <div :class="{ mycomment: true, 'mycomment-active': isClick }">
          <div class="top">
            <div class="left">
              <img :src="idxPhotoUrl" class="image" v-if="idxPhoto !== 0" />
              <img
                src="../assets/pic-8.jpg"
                class="image"
                v-if="idxPhoto === 0"
              />
            </div>
            <div class="right">
              <textarea
                placeholder="期待你的精彩评论！(*^▽^*)"
                v-model="myComment"
                @input="changeInput"
                @click="changeClick(true)"
                @blur="changeClick(false)"
                :class="{ 'text-active': isInput }"
              ></textarea>
            </div>
          </div>
          <div
            class="button"
            @click="submit()"
            @mouseenter="changeEnter(true)"
            @mouseleave="changeEnter(false)"
          >
            发布
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Header from "@/components-1/Header.vue";
import Voice from "@/components-1/Voice.vue";
import Model from "@/components-1/Model.vue";
import CommentMark from "@/components-1/CommentMark.vue";
import { ElNotification, ElMessage } from "element-plus";
import { Viewer } from "@bytemd/vue-next";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/user";
import { getCookie } from "@/utils/cookies";
import Comment from "@/components-1/CommentMain.vue";
import { ref, getCurrentInstance, onMounted } from "vue";
import { getDate } from "@/utils/getDate";
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const { ip } = storeToRefs(userStore);
const { messageUncheck } = storeToRefs(userStore);

let isDeleteTip = ref(false);
let isCommentMark = ref(false);
let curMarkCommentNode;
let markData;
let markCommentData;
let commentMark = ref([]);
let curId = ref(1);
let isTip = ref(false);
let catalog = ref();
let isHide = ref(true);
let tooltype = ref(0);
let iszero = ref(true);
let url;
let isEnter = ref(false);
let myComment = ref();
let isInput = ref(false);
let isClick = ref(false);
let tag = ref([{ tag: "无" }]);
let editType = ref(1);
let idxArticle = ref();
let title = ref();
let subtitle = ref();
let authorName = ref();
let idxAuthor = ref();
let content = ref();
let gmtCreate = ref();
let gmtModify = ref();
let comment = ref([]);
let summary = ref();
let articleCollect = ref();
let articleLike = ref();
let articleView = ref();
let articlePhotoUrl = ref(0);
let isLike = ref(0);
let isCollect = ref(0);
let authorPhotoUrl = ref(0);
let idxHistory;
let cataArr = ref([]);
var doms = "";
let range;
let textNodes;
let idx = 0;
let clickId = 0;
let serializeData = [];
let serializeDataForComment = [];
let idxForComment = 0;
let isSendMarkComment = false;
let markCommentContent = ref("");
let clickCommentId;
let cataType = ref(1);
let isCataScroll = false;
let isCommentScroll = false;

let idxPhoto = ref(
  Number(getCookie("idxPhoto").idxPhoto)
    ? Number(getCookie("idxPhoto").idxPhoto)
    : 0
);
let idxPhotoUrl = ref(
  getCookie("idxPhotoUrl").idxPhotoUrl
    ? getCookie("idxPhotoUrl").idxPhotoUrl
    : 0
);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);

const changeCataType = (num) => {
  cataType.value = num;
};

const changeToolType = (num) => {
  if (tooltype.value === num && isHide.value === false) {
    isHide.value = true;
  } else {
    tooltype.value = num;
    isHide.value = false;
  }
};

const submit = () => {
  if (myComment.value) {
    proxy.$axios
      .post("/comment/insert", {
        idxArticle: idxArticle.value,
        idxUser: idxUser.value,
        idxComment: 0,
        ip: ip.value[0],
        userName: userName.value,
        userPhoto: idxPhoto.value,
        content: myComment.value,
        gmtCreate: getDate(),
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          myComment.value = "";
          isClick.value = false;
          ElMessage({
            showClose: false,
            message: "提交成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
          getMainComment();
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          ElMessage({
            showClose: false,
            message: "登录失效",
            type: "warning",
            duration: 1000,
            center: true,
            grouping: false,
          });
          setTimeout(() => {
            proxy.$router.push({ name: "login" });
          }, 500);
        }
      });
  } else {
    ElMessage({
      showClose: false,
      message: "评论内容不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
  isClick.value = false;
};

const changeInput = (e) => {
  if (myComment.value) {
    isInput.value = true;
  } else {
    isInput.value = false;
  }
};

const changeClick = (state) => {
  if (!isEnter.value) {
    isClick.value = state;
  }
};

const openUserInfo = () => {
  if (getCookie("token").token) {
    let userRoute = proxy.$router.resolve({
      name: "user",
      query: {
        id: idxUser.value,
      },
    });
    window.open(userRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openOtherInfo = () => {
  let otherRoute = proxy.$router.resolve({
    name: "other",
    query: {
      id: idxAuthor.value,
    },
  });
  window.open(otherRoute.href, "_blank");
};

const changeEnter = (state) => {
  isEnter.value = state;
};

const openUserMessage = () => {
  if (getCookie("token").token) {
    let messageRoute = proxy.$router.resolve({
      name: "message",
      query: {
        userName: encodeURIComponent(userName.value),
      },
    });
    window.open(messageRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openEdit = () => {
  if (getCookie("token").token) {
    let editRoute = proxy.$router.resolve({
      name: "edit",
      query: {
        userName: encodeURIComponent(userName.value),
      },
    });
    window.open(editRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const changeLike = () => {
  if (isLike.value === 0) {
    isLike.value = 1;
    articleLike.value = articleLike.value + 1;
    updateHistory();
  } else {
    isLike.value = 0;
    articleLike.value = articleLike.value - 1;
    updateHistory();
  }
};

const changeCollect = () => {
  if (isCollect.value === 0) {
    isCollect.value = 1;
    articleCollect.value = articleCollect.value + 1;
    updateHistory();
    updateCollect();
  } else {
    isCollect.value = 0;
    articleCollect.value = articleCollect.value - 1;
    updateHistory();
    updateCollect();
  }
};

const scrollComment = () => {
  isCommentScroll = true;
  let comment = document.getElementsByClassName("comment")[0];
  comment.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
  setTimeout(() => {
    isCommentScroll = false;
  }, 2000);
};

const scrollCatalog = () => {
  let comment = document.getElementsByClassName("current-cata")[0];
  if (comment != undefined) {
    comment.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  }
};

const changeShare = () => {};

const selectContent = () => {
  let selObj = window.getSelection();
  range = selObj.getRangeAt(0);
  isDeleteTip.value = false;
  if (range.collapsed) {
    isTip.value = false;
    if (!isSendMarkComment && isCommentMark.value) {
      cancelCommentMark();
    }
    isCommentMark.value = false;
    return;
  } else {
    if (!isDeleteTip.value) {
      isTip.value = true;
      setTip(range);
    }
  }
};

const changeTip = () => {
  isTip.value = false;
};

const setTip = () => {
  let { left, top, width } = range.getBoundingClientRect();
  let scrollTop = document.documentElement.scrollTop;
  let tipLeft = left + (width - 120) / 2;
  let tipTop = top - 40 + scrollTop;
  let tip = document.getElementsByClassName("tip")[0];
  if (tip != undefined) {
    tip.style.left = tipLeft + "px";
    tip.style.top = tipTop + "px";
  }
};

const setCommentMark = () => {
  let { left, bottom, width } = range.getBoundingClientRect();
  isCommentMark.value = true;
  isSendMarkComment = false;
  isTip.value = false;
  let scrollTop = document.documentElement.scrollTop;
  let tipLeft = left + (width - 320) / 2;
  let tipTop = bottom + 10 + scrollTop;
  let tip = document.getElementsByClassName("comment-mark")[0];
  if (tip != undefined) {
    tip.style.left = tipLeft + "px";
    tip.style.top = tipTop + "px";
  }

  window.getSelection().removeAllRanges();
  markForComment();
};

const setDeleteTip = () => {
  let { left, top, width } = range.getBoundingClientRect();
  let scrollTop = document.documentElement.scrollTop;
  let tipLeft = left + (width - 60) / 2;
  let tipTop = top - 40 + scrollTop;
  let tip = document.getElementsByClassName("delete-tip")[0];
  if (tip != undefined) {
    tip.style.left = tipLeft + "px";
    tip.style.top = tipTop + "px";
  }
};

const markForComment = () => {
  textNodes = [];
  let { commonAncestorContainer, startContainer, endContainer } = range;
  walk(commonAncestorContainer, (node) => {
    if (
      node === startContainer ||
      node === endContainer ||
      range.isPointInRange(node, 0)
    ) {
      // 起始和结束节点，或者在范围内的节点，如果是文本节点则收集起来
      if (node.nodeType === 3) {
        textNodes.push(node);
      }
    }
  });
  handleTextNodesForComment();
};

const handleTextNodesForComment = () => {
  // 生成本次的唯一id
  let id = ++idxForComment;
  // 遍历文本节点
  if (textNodes !== undefined) {
    textNodes.forEach((node) => {
      // 范围的首尾元素需要判断一下偏移量，用来截取字符
      let startOffset = 0;
      let endOffset = node.nodeValue.length;
      if (node === range.startContainer && range.startOffset !== 0) {
        startOffset = range.startOffset;
      }
      if (node === range.endContainer && range.endOffset !== 0) {
        endOffset = range.endOffset;
      }
      // 替换该文本节点
      replaceTextNodeForComment(node, id, startOffset, endOffset);
    });
  }

  // 序列化进行存储，获取刚刚生成的所有该id的划线元素
  serializeForComment(
    proxy.$refs.contentRef.querySelectorAll(".comment_id_" + id)
  );
};

const serializeForComment = (markNodes) => {
  // 选择article元素作为根元素，这样的好处是页面的其他结构如果改变了不影响划线元素的定位
  let root = proxy.$refs.contentRef;
  // 遍历刚刚生成的本次划线的所有span节点
  if (markNodes !== undefined) {
    markNodes.forEach((markNode) => {
      // 计算该字符离外层第一个非划线元素的总的文本偏移量
      let offset = getTextOffsetForComment(markNode);
      // 找到外层第一个非划线元素
      let { tagName, index } = getWrapNodeForComment(markNode, root);
      // 保存相关数据
      let textLength = markNode.textContent.length;
      if (textLength > 0) {
        serializeDataForComment.push({
          tagName,
          index,
          offset,
          length: textLength,
          id: markNode.getAttribute("data-comment-id"),
        });
      }
    });
  }

  //console.log(serializeDataForComment);
};

const mark = () => {
  textNodes = [];
  window.getSelection().removeAllRanges();
  let { commonAncestorContainer, startContainer, endContainer } = range;
  walk(commonAncestorContainer, (node) => {
    if (
      node === startContainer ||
      node === endContainer ||
      range.isPointInRange(node, 0)
    ) {
      // 起始和结束节点，或者在范围内的节点，如果是文本节点则收集起来
      if (node.nodeType === 3) {
        textNodes.push(node);
      }
    }
  });
  handleTextNodes();
  isTip.value = false;
};

function walk(node, callback = () => {}) {
  callback(node);
  if (node && node.childNodes) {
    for (let i = 0; i < node.childNodes.length; i++) {
      walk(node.childNodes[i], callback);
    }
  }
}

const handleTextNodes = () => {
  // 生成本次的唯一id
  let id = ++idx;
  // 遍历文本节点
  if (textNodes !== undefined) {
    textNodes.forEach((node) => {
      // 范围的首尾元素需要判断一下偏移量，用来截取字符
      let startOffset = 0;
      let endOffset = node.nodeValue.length;
      if (node === range.startContainer && range.startOffset !== 0) {
        startOffset = range.startOffset;
      }
      if (node === range.endContainer && range.endOffset !== 0) {
        endOffset = range.endOffset;
      }
      // 替换该文本节点
      replaceTextNode(node, id, startOffset, endOffset);
    });
  }

  // 序列化进行存储，获取刚刚生成的所有该id的划线元素
  serialize(proxy.$refs.contentRef.querySelectorAll(".mark_id_" + id));
};

const serialize = (markNodes) => {
  // 选择article元素作为根元素，这样的好处是页面的其他结构如果改变了不影响划线元素的定位
  let root = proxy.$refs.contentRef;
  // 遍历刚刚生成的本次划线的所有span节点
  if (markNodes !== undefined) {
    markNodes.forEach((markNode) => {
      // 计算该字符离外层第一个非划线元素的总的文本偏移量
      let offset = getTextOffset(markNode);
      // 找到外层第一个非划线元素
      let { tagName, index } = getWrapNode(markNode, root);
      // 保存相关数据
      let textLength = markNode.textContent.length;
      if (textLength > 0) {
        serializeData.push({
          tagName,
          index,
          offset,
          length: textLength,
          id: markNode.getAttribute("data-id"),
        });
      }
    });
  }
  updateMark();
  //console.log(serializeData);
};

const getTextOffset = (node) => {
  let offset = 0;
  let parNode = node;
  // 遍历直到外层第一个非划线元素
  while (parNode && parNode.classList.contains("markLine")) {
    // 获取前面的兄弟元素的总字符数
    offset += getPrevSiblingOffset(parNode);
    parNode = parNode.parentNode;
  }
  return offset;
};

const getTextOffsetForComment = (node) => {
  let offset = 0;
  let parNode = node;
  // 遍历直到外层第一个非划线元素
  while (parNode && parNode.classList.contains("markLineComment")) {
    // 获取前面的兄弟元素的总字符数
    offset += getPrevSiblingOffset(parNode);
    parNode = parNode.parentNode;
  }
  return offset;
};

const getPrevSiblingOffset = (node) => {
  let offset = 0;
  let prevNode = node.previousSibling;
  while (prevNode) {
    offset +=
      prevNode.nodeType === 3
        ? prevNode.nodeValue.length
        : prevNode.textContent.length;
    prevNode = prevNode.previousSibling;
  }
  return offset;
};

const getWrapNodeForComment = (node, root) => {
  // 找到外层第一个非划线元素
  let wrapNode = node.parentNode;
  while (wrapNode.classList.contains("markLineComment")) {
    wrapNode = wrapNode.parentNode;
  }
  let wrapNodeTagName = wrapNode.tagName;
  // 计算索引
  let wrapNodeIndex = -1;
  // 使用标签选择器获取所有该标签元素
  let els = root.getElementsByTagName(wrapNodeTagName);
  els = [...els]
    .filter((item) => {
      // 过滤掉划线元素
      return !item.classList.contains("markLineComment");
    })
    .forEach((item, index) => {
      // 计算当前元素在其中的索引
      if (wrapNode === item) {
        wrapNodeIndex = index;
      }
    });
  return {
    tagName: wrapNodeTagName,
    index: wrapNodeIndex,
  };
};

const getWrapNode = (node, root) => {
  // 找到外层第一个非划线元素
  let wrapNode = node.parentNode;
  while (wrapNode.classList.contains("markLine")) {
    wrapNode = wrapNode.parentNode;
  }
  let wrapNodeTagName = wrapNode.tagName;
  // 计算索引
  let wrapNodeIndex = -1;
  // 使用标签选择器获取所有该标签元素
  let els = root.getElementsByTagName(wrapNodeTagName);
  els = [...els]
    .filter((item) => {
      // 过滤掉划线元素
      return !item.classList.contains("markLine");
    })
    .forEach((item, index) => {
      // 计算当前元素在其中的索引
      if (wrapNode === item) {
        wrapNodeIndex = index;
      }
    });
  return {
    tagName: wrapNodeTagName,
    index: wrapNodeIndex,
  };
};

const replaceTextNodeForComment = (node, id, startOffset, endOffset) => {
  // 创建一个文档片段用来替换文本节点
  let fragment = document.createDocumentFragment();
  let startNode = null;
  let endNode = null;
  // 截取前一段不需要划线的文本
  if (startOffset !== 0) {
    startNode = document.createTextNode(node.nodeValue.slice(0, startOffset));
  }
  // 截取后一段不需要划线的文本
  if (endOffset !== 0) {
    endNode = document.createTextNode(node.nodeValue.slice(endOffset));
  }
  startNode && fragment.appendChild(startNode);

  // 创建一个span标签用来作为划线包裹元素
  let textNode = document.createElement("span");
  textNode.className = "markLineComment comment_id_" + id;
  textNode.style = "border-bottom: 2px dashed rgb(241,31,31);";
  textNode.setAttribute("data-comment-id", id);
  textNode.textContent = node.nodeValue.slice(startOffset, endOffset);
  fragment.appendChild(textNode);

  endNode && fragment.appendChild(endNode);
  // 替换文本节点
  node.parentNode.replaceChild(fragment, node);
};

const replaceTextNode = (node, id, startOffset, endOffset) => {
  // 创建一个文档片段用来替换文本节点
  let fragment = document.createDocumentFragment();
  let startNode = null;
  let endNode = null;
  // 截取前一段不需要划线的文本
  if (startOffset !== 0) {
    startNode = document.createTextNode(node.nodeValue.slice(0, startOffset));
  }
  // 截取后一段不需要划线的文本
  if (endOffset !== 0) {
    endNode = document.createTextNode(node.nodeValue.slice(endOffset));
  }
  startNode && fragment.appendChild(startNode);

  // 创建一个span标签用来作为划线包裹元素
  let textNode = document.createElement("span");
  textNode.className = "markLine mark_id_" + id;
  textNode.style = "color: rgb(241,31,31); background-color: #fde2e2;";
  textNode.setAttribute("data-id", id);
  textNode.textContent = node.nodeValue.slice(startOffset, endOffset);
  fragment.appendChild(textNode);

  endNode && fragment.appendChild(endNode);
  // 替换文本节点
  node.parentNode.replaceChild(fragment, node);
};

// 显示取消划线的tooltip
const showCancelTip = (e) => {
  let tar = e.target;
  if (tar.classList.contains("markLine") && isTip.value === false) {
    e.stopPropagation();
    e.preventDefault();
    // 获取划线id
    clickId = tar.getAttribute("data-id");
    // 获取该id的所有划线元素
    let markNodes = document.querySelectorAll(".mark_id_" + clickId);
    // 选择第一个和最后一个文本节点来作为range边界
    let startContainer = markNodes[0].firstChild;
    let endContainer = markNodes[markNodes.length - 1].lastChild;
    range = document.createRange();
    range.setStart(startContainer, 0);
    range.setEnd(endContainer, endContainer.nodeValue.length);
    isDeleteTip.value = true;
    setDeleteTip();
  }
  if (tar.classList.contains("markLineComment")) {
    clickCommentId = tar.getAttribute("data-comment-id");
    tooltype.value = 3;
    isHide.value = false;
    clearMarkCommentStyle();
    setTimeout(() => {
      scrollMarkComment(clickCommentId);
    }, 500);

    tar = tar.parentNode;
    if (tar.classList.contains("markLine") && isTip.value === false) {
      e.stopPropagation();
      e.preventDefault();
      // 获取划线id
      clickId = tar.getAttribute("data-id");
      // 获取该id的所有划线元素
      let markNodes = document.querySelectorAll(".mark_id_" + clickId);
      // 选择第一个和最后一个文本节点来作为range边界
      let startContainer = markNodes[0].firstChild;
      let endContainer = markNodes[markNodes.length - 1].lastChild;
      range = document.createRange();
      range.setStart(startContainer, 0);
      range.setEnd(endContainer, endContainer.nodeValue.length);
      isDeleteTip.value = true;
      setDeleteTip();
    }
  }
};

const sendMarkComment = () => {
  isSendMarkComment = true;
  isCommentMark.value = false;
  if (markCommentContent.value) {
    proxy.$axios
      .post("/comment/insert", {
        idxArticle: idxArticle.value,
        idxUser: idxUser.value,
        idxComment: 0,
        ip: ip.value[0],
        userName: userName.value,
        userPhoto: idxPhoto.value,
        content: markCommentContent.value,
        idxSpan: idxForComment,
        gmtCreate: getDate(),
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          markCommentContent.value = "";
          ElMessage({
            showClose: false,
            message: "提交成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
          getMainComment();
          tooltype.value = 3;
          isHide.value = false;

          clearMarkCommentStyle();
          setTimeout(() => {
            scrollMarkComment(idxForComment);
          }, 1000);

          proxy.$axios
            .post("/article/update/mark", {
              id: Number(idxArticle.value),
              markData: JSON.stringify(serializeDataForComment),
              totalMark: idxForComment,
            })
            .then((response) => {})
            .catch((error) => {});
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          ElMessage({
            showClose: false,
            message: "登录失效",
            type: "warning",
            duration: 1000,
            center: true,
            grouping: false,
          });
          setTimeout(() => {
            proxy.$router.push({ name: "login" });
          }, 500);
        }
      });
  } else {
    ElMessage({
      showClose: false,
      message: "评论内容不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const cancelCommentMark = () => {
  let markNodes = document.querySelectorAll(".comment_id_" + idxForComment);
  for (let i = 0; i < markNodes.length; i++) {
    let item = markNodes[i];
    let fregment = document.createDocumentFragment();
    for (let j = 0; j < item.childNodes.length; j++) {
      fregment.appendChild(item.childNodes[j].cloneNode(true));
    }
    item.parentNode.replaceChild(fregment, item);
  }
  idxForComment--;
  serializeDataForComment = serializeData.filter((item) => {
    return item.id !== clickId;
  });
};

const cancelMark = () => {
  isDeleteTip.value = false;
  let markNodes = document.querySelectorAll(".mark_id_" + clickId);
  for (let i = 0; i < markNodes.length; i++) {
    let item = markNodes[i];
    let fregment = document.createDocumentFragment();
    for (let j = 0; j < item.childNodes.length; j++) {
      fregment.appendChild(item.childNodes[j].cloneNode(true));
    }
    item.parentNode.replaceChild(fregment, item);
  }

  serializeData = serializeData.filter((item) => {
    return item.id !== clickId;
  });
  updateMark();
  //console.log(serializeData)
};

const deserialization = () => {
  let root = proxy.$refs.contentRef;
  console.log(markData);
  markData.forEach((item) => {
    let wrapNode = root.getElementsByTagName(item.tagName)[item.index];
    let len = 0;
    let end = false;
    let first = true;
    let _length = item.length;
    let _offset = item.offset;
    walk(wrapNode, (node) => {
      if (end) {
        return;
      }
      if (node != undefined && node.nodeType === 3) {
        let nodeTextLength = node.nodeValue.length;
        if (len + nodeTextLength > _offset) {
          // startOffset之前的文本不需要划线
          let startOffset = first ? item.offset - len : 0;
          first = false;
          // 如果该文本节点剩余的字符数量小于划线文本的字符长度的话代表该文本节点还只是划线文本的一部分，
          // 还需要到下一个文本节点里去处理
          let endOffset =
            startOffset +
            (nodeTextLength - startOffset >= _length
              ? _length
              : nodeTextLength - startOffset);
          replaceTextNode(node, item.id, startOffset, endOffset);
          // 长度需要减去之前节点已经处理掉的长度
          _length = _length - (nodeTextLength - startOffset);
          // 如果剩余要处理的划线文本的字符数量为0代表已经处理完了，可以结束了
          if (_length <= 0) {
            end = true;
          }
        }
        len += nodeTextLength;
      }
    });
  });
};

const deserializationForComment = () => {
  let root = proxy.$refs.contentRef;
  markCommentData.forEach((item) => {
    let wrapNode = root.getElementsByTagName(item.tagName)[item.index];
    let len = 0;
    let end = false;
    let first = true;
    let _length = item.length;
    let _offset = item.offset;
    walk(wrapNode, (node) => {
      if (end) {
        return;
      }
      if (node != undefined && node.nodeType === 3) {
        let nodeTextLength = node.nodeValue.length;
        if (len + nodeTextLength > _offset) {
          // startOffset之前的文本不需要划线
          let startOffset = first ? item.offset - len : 0;
          first = false;
          // 如果该文本节点剩余的字符数量小于划线文本的字符长度的话代表该文本节点还只是划线文本的一部分，
          // 还需要到下一个文本节点里去处理
          let endOffset =
            startOffset +
            (nodeTextLength - startOffset >= _length
              ? _length
              : nodeTextLength - startOffset);
          replaceTextNodeForComment(node, item.id, startOffset, endOffset);
          // 长度需要减去之前节点已经处理掉的长度
          _length = _length - (nodeTextLength - startOffset);
          // 如果剩余要处理的划线文本的字符数量为0代表已经处理完了，可以结束了
          if (_length <= 0) {
            end = true;
          }
        }
        len += nodeTextLength;
      }
    });
  });
};

document.body.onscroll = () => {
  let scrollTop = document.documentElement.scrollTop;
  let comment = document.getElementsByClassName("comment")[0];
  let mycomment = document.getElementsByClassName("mycomment")[0];
  if (scrollTop >= 300) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    if (fixHead != undefined) {
      fixHead.style.display = "flex";
    }
  } else if (scrollTop < 300) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    if (fixHead != undefined) {
      fixHead.style.display = "none";
    }
  }
  if (mycomment != undefined && comment != undefined) {
    if (scrollTop + window.innerHeight >= comment.offsetTop + 230) {
      mycomment.style.display = "block";
    } else {
      mycomment.style.display = "none";
    }
  }
  if (doms != "") {
    for (let i = doms.length - 1; i >= 0; i--) {
      if (parseInt(scrollTop) >= Math.ceil(doms[i].offsetTop) - 600) {
        let id = doms[i].getAttribute("id");
        curId.value = Number(id.slice(10));
        break;
      }
    }
  }
  if (!isCataScroll && !isCommentScroll) {
    scrollCatalog();
  }
};

const updateCollect = () => {
  url = `/user/update/collect?idxUser=${idxUser.value}&idxArticle=${idxArticle.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {});
};

const updateHistory = () => {
  proxy.$axios
    .post("/user/update/history", {
      id: idxHistory,
      isLike: isLike.value,
      isCollect: isCollect.value,
      isComment: 0,
      isShare: 0,
      gmtModify: getDate(),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {});
};

const updateMark = () => {
  console.log(serializeData);
  proxy.$axios
    .post("/user/update/mark", {
      id: idxHistory,
      markData: JSON.stringify(serializeData),
      gmtModify: getDate(),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {});
};

const makeCatalogue = () => {
  var eles = ["h1", "h2", "h3", "h4", "h5", "h6"];
  doms = proxy.$refs.contentRef.querySelectorAll(eles.toString());
  if (!doms || !doms.length) {
    return;
  }
  let index = 0;
  for (let h of doms) {
    let tag = h.nodeName.toLowerCase();
    if (!eles.includes(tag)) {
      continue;
    }
    let id = `catalogue_${++index}`;
    h.setAttribute("id", id);
    let text = h.innerHTML.replace(/<\/?[^>]+>/g, "");
    cataArr.value.push({ text: text, href: id, class: tag });
  }
};

const scrollCata = (id) => {
  isCataScroll = true;
  document.querySelector(`#${id}`).scrollIntoView({
    behavior: "smooth",
  });
  setTimeout(() => {
    isCataScroll = false;
  }, 2000);
};

const scrollMarkComment = (id) => {
  let nodeComment = document.querySelector(`.markComment-id-${id}`);
  curMarkCommentNode = nodeComment;
  nodeComment.scrollIntoView({
    behavior: "smooth",
  });
  nodeComment.style.backgroundColor = "#fde2e2";
  nodeComment.style.boxShadow = "0 4px 16px rgba(0,0,0,0.15)";
};

const clearMarkCommentStyle = () => {
  if (curMarkCommentNode !== undefined) {
    curMarkCommentNode.style.backgroundColor = "white";
    curMarkCommentNode.style.boxShadow = "none";
  }
};

const getMainComment = () => {
  url = `/comment/get/main?idxArticle=${idxArticle.value}&idxUser=${idxUser.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        comment.value = response.data.data.record;
        commentMark.value = [];
        for (let i = 0; i < comment.value.length; i++) {
          if (comment.value[i].idxSpan !== 0) {
            commentMark.value.push(comment.value[i]);
          }
        }
        if (comment.value.length) {
          iszero.value = false;
        } else {
          iszero.value = true;
        }
      }
    })
    .catch((error) => {});
};

onMounted(() => {
  idxArticle.value = decodeURIComponent(proxy.$route.query.idxArticle);

  url = `/article/get/read?idxArticle=${idxArticle.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        idxAuthor.value = response.data.data.idxUser;
        authorName.value = response.data.data.userName;
        title.value = response.data.data.title;
        subtitle.value = response.data.data.subtitle;
        summary.value = response.data.data.summary;
        articleView.value = response.data.data.articleView;
        articleLike.value = response.data.data.articleLike;
        articleCollect.value = response.data.data.articleCollect;
        articlePhotoUrl.value = response.data.data.articlePhotoUrl;
        authorPhotoUrl.value = response.data.data.userPhotoUrl;
        idxForComment = response.data.data.totalMark;
        content.value = response.data.data.content;
        gmtCreate.value = response.data.data.gmtCreate;
        gmtModify.value = response.data.data.gmtModify;
        editType.value = response.data.data.editType;
        tag.value = response.data.data.tagList;
        if (
          response.data.data.markData !== null &&
          response.data.data.markData !== ""
        ) {
          markCommentData = JSON.parse(response.data.data.markData);
          serializeDataForComment = markCommentData;
        }

        setTimeout(() => {
          makeCatalogue();
        }, 500);

        setTimeout(() => {
          if (markData !== null && markData != "") {
            deserialization();
          }
          if (markCommentData !== null && markCommentData !== undefined) {
            deserializationForComment();
          }
        }, 500);
      }
    })
    .catch((error) => {});

  url = `/comment/get/main?idxArticle=${idxArticle.value}&idxUser=${idxUser.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        comment.value = response.data.data.record;
        for (let i = 0; i < comment.value.length; i++) {
          if (comment.value[i].idxSpan !== 0) {
            commentMark.value.push(comment.value[i]);
          }
        }
        if (comment.value.length) {
          iszero.value = false;
        } else {
          iszero.value = true;
        }
      }
    })
    .catch((error) => {});

  if (idxUser.value !== 0) {
    url = `/user/get/history/read?idxUser=${idxUser.value}&idxArticle=${idxArticle.value}`;
    proxy.$axios
      .get(url)
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          isLike.value = response.data.data.isLike;
          isCollect.value = response.data.data.isCollect;
          idxHistory = response.data.data.id;
          markData = response.data.data.markData;
          if (markData !== null && markData !== "") {
            markData = JSON.parse(markData);
            serializeData = markData;
            idx = markData.length;
          }
        }
      })
      .catch((error) => {});
  }
});
</script>
<style scoped>
@keyframes icon {
  0% {
    margin-top: 0px;
    margin-bottom: 0px;
  }
  50% {
    margin-top: -5px;
    margin-bottom: 5px;
  }
  100% {
    margin-top: 0px;
    margin-bottom: 0px;
  }
}

.fix-head {
  width: 100%;
  height: 8vh;
  position: fixed;
  top: 0%;
  left: 0%;
  display: none;
  z-index: 10;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
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
  opacity: 0.9;
}
.fix-head .title {
  margin-left: 10%;
  text-align: center;
  user-select: none;
  height: 8vh;
  margin-top: 2vh;
  width: 20%;
  font-size: 20px;
  font-weight: 700;
  color: white;
}
.fix-head .tool {
  margin-left: 50%;
  display: flex;
}
.fix-head .tool .photo {
  width: 5vh;
  max-width: 6vh;
  cursor: pointer;
  margin-top: 1.5vh;
  margin-bottom: 1.5vh;
  margin-right: 1vh;
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.5s ease;
}
.fix-head .tool .photo:hover {
  width: 6vh;
  margin-top: 1vh;
  margin-bottom: 1vh;
  margin-right: 0vh;
}
.fix-head .tool .item {
  margin-left: 18px;
  user-select: none;
  display: flex;
  cursor: pointer;
  text-align: center;
  flex-direction: column;
  justify-content: center;
}
.fix-head .tool .item .icon {
  color: rgb(214, 31, 31);
  font-size: 28px;
  font-weight: 500;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.2));
  transition: all 0.5s ease;
}
.fix-head .tool .item .icon:hover {
  animation-name: icon;
  animation-duration: 0.3s;
  animation-timing-function: ease;
  animation-iteration-count: 1;
}
.fix-head .tool .item .text {
  margin-top: -6px;
  font-size: 12px;
  font-weight: 400;
  color: rgb(214, 31, 31);
}
.read-main {
  width: 100%;
  height: 100vh;
}
.read-main .wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 92vh;
  justify-content: center;
  padding-top: 5vh;
  background-color: rgba(97, 97, 97, 0.05);
}
.read-main .wrapper .middle {
  display: flex;
  flex-direction: column;
  width: 50%;
  padding: 50px;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.read-main .middle .title {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 1vh;
}
.read-main .middle .title .main {
  font-size: 22px;
  font-weight: 600;
}
.read-main .middle .title .sub {
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 18px;
  font-weight: 500;
}
.read-main .middle .info {
  display: flex;
  user-select: none;
  margin-top: 1vh;
  margin-bottom: 3vh;
  justify-content: space-between;
  flex-direction: row;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}
.read-main .middle .author {
  display: flex;
  flex-direction: row;
  margin-bottom: 3vh;
}
.read-main .middle .author .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.image {
  width: 100%;
  height: 100%;
}
.user-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.read-main .middle .author .name {
  font-size: 16px;
  font-weight: 600;
  margin-left: 10px;
  user-select: none;
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
  color: rgb(214, 31, 31);
}
.read-main .middle .summary {
  font-size: 16px;
  line-height: 25px;
  font-weight: 400;
}
.read-main .middle .article-photo {
  margin-top: 3vh;
  margin-bottom: 3vh;
  width: 100%;
  max-height: 20vh;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.article-photo .image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.read-main .middle .content {
  font-size: 20px;
  font-weight: 400;
  line-height: 30px;
  font-family: "仿宋";
  white-space: pre-wrap;
  word-wrap: break-word;
}
.read-main .middle .other {
  display: flex;
  margin-top: 1vh;
  height: 30px;
  line-height: 31px;
  justify-content: space-between;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
  user-select: none;
}
.read-main .other .pre {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.read-main .other .item {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.read-main .tag {
  display: flex;
  user-select: none;
}
.tag-item {
  margin-right: 10px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.tag-item:hover {
  color: rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.read-main .comment {
  width: 50%;
  padding: 50px;
  min-height: 20vh;
  margin-bottom: 5vh;
  margin-top: 5vh;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.mycomment {
  position: fixed;
  display: none;
  width: 50%;
  left: 50%;
  top: 90vh;
  height: 15vh;
  transform: translateX(-50%);
  padding-left: 50px;
  padding-right: 50px;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-color: white;
  transition: all 0.5s ease;
}
.mycomment-active {
  top: 85vh;
  transition: all 0.5s ease;
}
.mycomment .top {
  width: 100%;
  height: 6vh;
  margin-top: 2vh;
  display: flex;
}
.mycomment .top .left {
  width: 5vh;
  height: 5vh;
  margin: auto;
  margin-left: 0;
  margin-right: 0;
  border-radius: 50%;
}
.top .right {
  margin-left: 15px;
  border-radius: 8px;
  border: 1px solid rgb(214, 31, 31);
  width: calc(100% - 5vh - 15px);
  height: 6vh;
}
.right textarea {
  outline: none;
  resize: none;
  border: none;
  margin-top: 0.5vh;
  padding-top: 1vh;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 8px;
  width: calc(100% - 20px);
  height: 4vh;
  font-size: 16px;
  font-weight: 500;
  line-height: 22px;
  color: black;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.right .text-active {
  padding-top: 0.5vh;
  height: 5vh;
}
.mycomment .button {
  margin-left: 90%;
  width: 10%;
  height: 3.5vh;
  margin-top: 2vh;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  text-align: center;
  font-size: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
  user-select: none;
}
.left .image {
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  user-select: none;
}
.no-comment {
  width: 100%;
  height: 20vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  user-select: none;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.5);
  font-weight: 500;
}
.read-main .comment .comment-title {
  font-size: 18px;
  font-weight: 500;
}
.read-main .comment .comment-list {
  margin-top: 5vh;
  display: flex;
  flex-direction: column;
}
.right-side {
  position: fixed;
  top: 90%;
  left: 82%;
  width: 15%;
  background-color: white;
  height: 6.6vh;
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  border-radius: 8px;
  flex-direction: row;
  transition: all 0.3s ease;
}
.right-side:hover {
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
.right-side .item {
  width: 100%;
  height: 5vh;
  padding-top: 0.8vh;
  padding-bottom: 0.8vh;
  border-radius: 5px;
  text-align: center;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.3s ease;
}
.right-side .item:nth-child(4) {
  color: rgb(214, 31, 31);
}
.right-side .item:hover {
  background-color: #fde2e2;
  transition: all 0.3s ease;
}
.right-side .item .icon {
  font-size: 25px;
}
.right-side .item:hover {
  color: rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.right-side .item .data {
  margin-top: -5px;
  font-size: 12px;
  user-select: none;
}
.right-side .opt-active {
  color: rgb(214, 31, 31);
  background-color: #fde2e2;
  transition: all 0.2s ease;
}

:deep(.el-badge__content--danger) {
  background-color: rgb(214, 31, 31);
}
:deep(.el-badge__content) {
  border: none;
}
.catalogue {
  position: fixed;
  top: 13vh;
  left: 20px;
  width: 15%;
  padding-left: 10px;
  padding-top: 2vh;
  padding-bottom: 2vh;
  background-color: white;
  min-width: 100px;
  min-height: 30vh;
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  max-height: 80vh;
  overflow-x: hidden;
  overflow-y: auto;
  transition: all 0.3s ease;
  overflow: hidden;
}
.catalogue:hover {
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
.catalogue #h1 {
  font-size: 16px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  width: calc(100% - 40px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 10px;
}
.catalogue #h2 {
  font-size: 15px;
  margin-left: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  width: calc(100% - 50px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 5px;
}
.catalogue #h3 {
  font-size: 14px;
  margin-left: 20px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  width: calc(100% - 60px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 5px;
}

.catalogue #h4 {
  font-size: 14px;
  margin-left: 30px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  width: calc(100% - 70px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 5px;
}
.catalogue #h5 {
  font-size: 14px;
  margin-left: 40px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  margin-bottom: 5px;
  width: calc(100% - 80px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
}
.catalogue #h6 {
  font-size: 14px;
  margin-left: 50px;
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 10px;
  padding-right: 10px;
  line-height: 26px;
  margin-bottom: 5px;
  width: calc(100% - 90px);
  min-height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  border-radius: 8px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
}
.catalogue #h1:hover,
.catalogue #h2:hover,
.catalogue #h3:hover,
.catalogue #h4:hover,
.catalogue #h5:hover,
.catalogue #h6:hover {
  background-color: #fde2e2;
  font-weight: 600;
  color: black;
  transition: all 0.3s ease;
}
.current-cata {
  background-color: #fde2e2;
  font-weight: 600;
  color: red;
  transition: all 0.3s ease;
}
.catalogue .note {
  font-weight: 600;
  font-size: 18px;
  margin-top: 1vh;
  margin-bottom: 2vh;
  margin-left: 10px;
  height: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: rgba(0, 0, 0, 0.3);
}
.catalogue .cur-note {
  color: black;
}
.right-tool {
  position: fixed;
  top: 13vh;
  height: 72vh;
  left: calc(100vw - 80px);
  width: 64px;
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  background-color: white;
  transition: all 0.3s ease;
}
.right-tool:hover {
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
.tool-sp {
  position: fixed;
  top: 13vh;
  height: 72vh;
  left: calc(80vw - 80px);
  width: 20vw;
  border-radius: 8px;
  background-color: white;
  z-index: 10;
  border-right: 1px solid #eee;
  transition: all 0.3s ease;
}
.tool-sp-hide {
  width: 0vw;
  left: calc(100vw - 80px);
  transition: all 0.3s ease;
  opacity: 0;
  z-index: -10;
}
.model,
.voice {
  width: 100%;
  height: 100%;
}
.right-tool-list {
  height: 100%;
  width: 100%;
}
.tool-list-item {
  padding-left: 17px;
  padding-right: 17px;
  width: 30px;
  height: 30px;
  padding-top: 15px;
  padding-bottom: 15px;
  font-size: 28px;
  color: rgb(214, 31, 31);
  border-radius: 5px;
  transition: all 0.1s ease;
}
.tool-active {
  left: calc(80vw - 80px);
  padding-left: 20vw;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.tool-item-active {
  background-color: #fde2e2;
  transition: all 0.3s ease;
}
.tool-list-item:hover {
  background-color: #fde2e2;
  transition: all 0.1s ease;
}
.tip {
  position: absolute;
  left: 0;
  top: 0;
  width: 100px;
  height: 30px;
  padding-left: 10px;
  padding-right: 10px;
  text-align: center;
  font-size: 14px;
  font-weight: 600;
  border-radius: 5px;
  background-color: #fde2e2;
  color: rgb(214, 31, 31);
  display: flex;
  justify-content: space-around;

  user-select: none;
}
.tip .tip-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
}
.delete-tip {
  position: absolute;
  left: 0;
  top: 0;
  width: 60px;
  height: 30px;
  text-align: center;
  font-size: 14px;
  font-weight: 600;
  border-radius: 5px;
  background-color: #fde2e2;
  color: rgb(214, 31, 31);
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
  user-select: none;
}
.comment-mark {
  position: absolute;
  left: 0;
  top: 0;
  width: 300px;
  height: 220px;
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 10px;
  border-radius: 8px;
  background-color: #fde2e2;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.comment-mark textarea {
  width: calc(100% - 20px);
  height: calc(100% - 55px);
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
  font-size: 16px;
  outline: none;
  resize: none;
  border-radius: 8px;
  background-color: white;
  border: none;
  line-height: 25px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.comment-mark .mark-button {
  margin-left: calc(100% - 70px);
  width: 60px;
  height: 26px;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  align-items: center;
  margin-bottom: 10px;
  margin-top: 5px;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  font-size: 14px;
  text-align: center;
  flex-direction: column;
  display: flex;
  justify-content: center;
}
.comment-sp {
  width: 100%;
  height: 100%;
}
.comment-sp .note {
  width: 100%;
  height: 40px;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-bottom: 2px solid #eee;
  user-select: none;
  color: rgb(214, 31, 31);
}
.comment-sp-list {
  padding-top: 10px;
  padding-left: 10px;
  padding-right: 10px;
  max-height: calc(100% - 50px);
  overflow-x: hidden;
  overflow-y: auto;
}

.catalogue .top-bar {
  display: flex;
}
</style>
