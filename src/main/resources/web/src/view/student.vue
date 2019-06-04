<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top bg-dark p-0 shadow-sm d-flex justify-content-between">
      <span class="navbar-brand col-sm-3 col-md-2 mr-0">SQL Judger</span>
      <div>
        <span class="navbar-text text-light mr-3 font-weight-bold">{{ currentStudent.name }}</span>
        <a class="navbar-text text-light mr-3" href="/view/login.html">退出</a>
      </div>
    </nav>
    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <nav class="nav nav-pills flex-column" id="nav_tabs">
              <a class="nav-link" data-toggle="pill" href="#home" role="tab">
                概览
              </a>
              <a class="nav-link active" data-toggle="pill" href="#exam" role="tab">
                考试安排
              </a>
            </nav>
          </div>
        </nav>

        <main class="col-md-9 ml-sm-auto col-lg-10 px-4 tab-content" role="main">
          <div class="tab-pane fade show" id="home" role="tabpanel">
            <h2 class="h2 mb-3">概览</h2>
            <h3 class="h3"></h3>
            <p>欢迎回来，{{ currentStudent.name }}</p>
          </div>
          <div class="tab-pane fade show active" id="exam" role="tabpanel">
            <h2 class="h2 mb-3">考试安排</h2>
            <div v-if=" this.exams.length === 0">
              <p>当前没有考试</p>
            </div>
            <div class="card mb-5 shadow-sm" v-for="exam in exams">
              <div class="card-header">
                <span class="h4">{{ exam.id }}. {{ exam.title }}</span>
                <span class="badge badge-primary ml-2">{{ getBadgeText(exam) }}</span>
              </div>
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <div>
                    <p class="card-text">考试时间: {{ unixEpochToString(exam.startTime) }} ~
                      {{ unixEpochToString(exam.endTime) }}</p>
                    <p class="card-text">试卷: {{ exam.testPaper.title }}</p>
                  </div>
                  <div class="align-middle">
                    <a :id="'btn-' + exam.id" class="btn btn-danger align-middle" target="_blank"
                       v-bind:href="'/view/student/exam.html?id=' + exam.id" v-if="answerable(exam)">进入考试
                    </a>
                    <button class="btn btn-primary" v-if="exam.status === 4" v-on:click="viewScore(exam)">查看分数</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="modal fade" id="scoreModal" ref="scoreModal" role="dialog" tabindex="-1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">考试分数</h5>
                    <button class="close" data-dismiss="modal" type="button">
                      <span>&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <p class="h5">{{ score.score ? score.score + '分' : '缺考' }}/{{ score.total }}分</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>
<style scoped> @import "./../lib/sidebar.css"; </style>
<style scoped> @import "./../lib/common.css"; </style>
<script>
    import moment from 'moment';
    import $ from 'jquery';
    import "bootstrap/dist/js/bootstrap.js"
    import axios from 'axios'

    import 'bootstrap/dist/css/bootstrap.css'
    import * as common from '../lib/commom.js'

    moment.locale('zh-cn');
    export default {
        name: 'app',
        data() {
            return {
                exercises: [],
                exams: [],
                score: {
                    total: null,
                    score: null,
                },
                currentStudent: {
                    name: "",
                    id: 0,
                    number: 0,
                    clazz: {}
                }
            }
        },
        async created() {
            this.loadExams();
            await axios.get("/student/profile")
                .then(res => {
                    if (res.data.status === 200) {
                        this.currentStudent = res.data.data;
                    } else {
                        console.log(res.data);
                        window.location.reload();
                    }
                });
        },
        methods: {
            answerable(exam) {
                if (exam.status !== 0 || moment().isAfter(exam.endTime * 1000))
                    return false;
                const diff = exam.startTime - moment().unix();
                if (diff <= 0)
                    return true;
                if (diff < 1800) {
                    setTimeout(() => {
                        $(`#btn-${exam.id}`).removeAttr("style");
                    }, diff * 1000);
                }
                return false;
            },
            unixEpochToString(second) {
                return moment.unix(second).format("YYYY-MM-DD HH:mm");
            },
            getBadgeText(exam) {
                if (exam.status === 1)
                    return "已取消";
                const now = moment();
                if (now.isBefore(exam.startTime * 1000)) {
                    return "未开始";
                }
                if (now.isAfter(exam.endTime * 1000)) {
                    return "已结束";
                }
                return "进行中";
            },
            loadTeachings() {
                common.load("/teacher/teaching", this.teachings);
            },
            loadExams() {
                common.load("/student/exam", this.exams);
            },
            viewScore(exam) {
                axios.get(`/student/exam/${exam.id}/score`)
                    .then(res => {
                        this.score = res.data.data;
                        $(this.$refs.scoreModal).modal('show');
                    })
            }
        }
    };
</script>