<template>
  <div class="footer-main">
    <div class="title"></div>
    <div class="about">
      <div class="service">
        <div class="type">
          <div class="type-title">服务</div>
          <div class="list">
            <div class="item">作品专栏</div>
            <div class="item">客服热线</div>
          </div>
        </div>
        <div class="type">
          <div class="type-title">推广</div>
          <div class="list">
            <div class="item">宣传页</div>
            <div class="item">APP</div>
            <div class="item">Windows</div>
          </div>
        </div>
        <div class="type">
          <div class="type-title">联系我们</div>
          <div class="list">
            <div class="item">邮箱</div>
            <div class="item">地址</div>
          </div>
        </div>
      </div>
      <div class="cert">
        <div class="item">
          <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank"
            >豫ICP备2024045786号</a
          >
        </div>
        <div class="item">
          <div class="icon"></div>
          <a
            href="https://beian.mps.gov.cn/#/query/webSearch?code=41170202411755"
            target="_blank"
            >豫公网安备41170202411755号</a
          >
        </div>
        <div class="item">© Dialogue - All Rights Reserved</div>
      </div>
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.footer-main {
  width: 100%;
  height: 40vh;
  margin-top: 10vh;
  display: flex;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-image: url("../assets/pic-8.jpg");
  background-position: center top;
  background-repeat: no-repeat;
  background-size: cover;
}
.footer-main .title {
  margin-left: 5%;
  width: 20%;
  height: 100%;
  background-image: url("../assets/title-3.png");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: contain;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.4));
}
.footer-main .about {
  margin-left: 10%;
  margin-right: 5%;
  margin-top: 5vh;
  width: 70%;
  display: flex;
  flex-direction: column;
  color:white;
}
.footer-main .about .service {
  width: 100%;
  height: 60%;
  margin-bottom: 1vh;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.footer-main .about .cert {
  display: flex;
}
.footer-main .about .cert .item {
  display: flex;
  margin-right: 20px;
  user-select: none;
}

.footer-main .about .cert .item .icon {
  width: 2vh;
  height: 2vh;
  padding-top: 1vh;
  margin-right: 3px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-image: url("../assets/cert-2.png");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: contain;
}
.footer-main .about .cert .item a {
  text-decoration: none;
  user-select: none;
  font-size: 16px;
  color:white;
}
.service .type {
  display: flex;
  margin-top: 3vh;
  flex-direction: column;
  justify-content: space-around;
  height: 20%;
  width: 100%;
  font-size: 18px;
  font-weight: 600;
  user-select: none;
}
.service .list {
  display: flex;
  height: 80%;
  width: 50%;
  flex-direction: column;
  font-size: 16px;
  font-weight: 400;
}
.service .list .item {
  margin-top: 15px;  
  cursor: pointer;
  user-select: none;
  transition: all 0.5s ease;
}
.service .list .item:hover{
  transform: translateX(10px);
  transition: all 0.5s ease;
}
</style>
