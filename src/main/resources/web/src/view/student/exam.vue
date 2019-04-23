<style>
  #main {
    height: 100vh;
  }

  #tab-container {
    max-height: calc(100vh - 10rem) !important;
  }

  .panel {
    height: unset !important;
    overflow-y: auto;
  }

  button.fixed-width {
    width: 5rem;
  }

  textarea.form-control {
    height: calc(100vh - 10rem) !important;
    resize: none;
  }

  #alert {
    position: fixed;
    top: 56px;
    left: 10rem;
    z-index: 999;
    right: 10rem;
  }
</style>
<style scoped> @import "./../../lib/sidebar.css"; </style>
<style scoped> @import "./../../lib/common.css"; </style>
<template>
  <div id="main">
    <div class="alert alert-danger alert-dismissible fade d-none" id="alert" role="alert">
      请注意考试即将结束
      <button aria-label="Close" class="close" data-dismiss="alert" type="button">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top bg-dark p-0 shadow-sm d-flex justify-content-between">
      <span class="navbar-brand ml-3">{{ exam.title }}</span>
      <span class="text-light mr-3 font-weight-bold">
        <countdown :interval="1000" :time="time" @end="onExamFinish" tag="span">
          <template slot-scope="props">{{ props.minutes }}:{{ props.seconds }}</template>
        </countdown>
      </span>
    </nav>
    <main class="container-fluid" role="main">
      <div id="tab-container">
        <div :class=" ['tab-panel fade row', showIndex === index ? 'show active' : 'd-none']" :id="'exercise-' + index"
             :key="index"
             role="tabpanel" v-for="(exerciseConfig, index) in exam.testPaper.exerciseConfigs">
          <div class="px-3 w-50 float-left panel">
            <div class="h3 d-flex justify-content-between">
              <span>{{ index + 1 }}. {{ exerciseConfig.exercise.title }}</span>
              <span class="badge badge-light">{{ exerciseConfig.score }}分</span>
            </div>
            <pre>{{ exerciseConfig.exercise.description }}</pre>
            <div class="mb-2">
            <span class="mt-1 font-weight-bold" data-toggle="collapse" v-bind:href="'#inputSQL-' + index">
              表结构
              <svg height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg">
            <path d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z"></path>
            <path d="M0 0h24v24H0z" fill="none"></path>
          </svg>
            </span>
              <span v-on:click="copySQL(index)">
              <svg height="24" viewBox="0 0 1024 1024" width="24" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M571.52 909.44H280.96c-61.44 0-111.36-49.92-111.36-111.36V387.2c0-61.44 49.92-111.36 111.36-111.36h290.56c61.44 0 111.36 49.92 111.36 111.36v410.88c0 61.44-49.92 111.36-111.36 111.36z m-290.56-569.6c-26.24 0-47.36 21.12-47.36 47.36v410.88c0 26.24 21.12 47.36 47.36 47.36h290.56c26.24 0 47.36-21.12 47.36-47.36V387.2c0-26.24-21.12-47.36-47.36-47.36H280.96z"
                fill="#515151"></path>
            <path
                d="M775.68 742.4c-17.92 0-32-14.08-32-32V333.44c0-66.56-53.76-120.32-120.32-120.32h-256c-17.92 0-32-14.08-32-32s14.08-32 32-32h256c101.76 0 184.32 82.56 184.32 184.32V710.4c0 17.28-14.08 32-32 32z"
                fill="#515151"></path>
          </svg>
            </span>
            </div>
            <div :id="'inputSQL-' + index" class="collapse">
              <pre class="code">{{ exerciseConfig.exercise.inputSQL }}</pre>
            </div>
            <span class="mt-1 font-weight-bold">初始数据</span>
            <div v-for="table in exerciseConfig.exercise.inputData">
              <label>表：{{ table.name }}</label>
              <table class="table table-striped">
                <thead>
                <tr>
                  <th scope="col" v-for="column in table.columns">{{ column }}</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="row in table.data">
                  <td v-for="item in row">{{ item }}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="px-3 w-50 float-left panel">
          <textarea class="form-control" placeholder="请写下 SQL 语句"
                    v-model="answerSheet.answers[index].inputSQL"></textarea>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between align-content-center p-3 fixed-bottom bg-white">
        <button :disabled="showIndex ===0 " class="btn btn-secondary fixed-width" v-on:click="prevPanel()">
          上一题
        </button>
        <div class="btn-toolbar" role="toolbar">
          <div class="btn-group btn-group-sm" role="group">
            <button
                :class="['btn', answerSheet.answers[n-1].inputSQL ? 'btn-info' : 'btn-outline-info']"
                v-for="n in exam.testPaper.exerciseConfigs.length" v-on:click="setPanel(n)">{{n}}
            </button>
          </div>
        </div>
        <button class="btn btn-primary fixed-width" v-on:click="nextPanel()">
          {{ showIndex === exam.testPaper.exerciseConfigs.length - 1 ? '交卷' : '下一题'}}
        </button>
      </div>
    </main>
    <div class="modal fade" id="submitModal" role="dialog" tabindex="-1">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">交卷</h5>
            <button aria-label="Close" class="close" data-dismiss="modal" type="button">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            试卷提交后不能继续答题，确定交卷？
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" type="button" v-on:click="submitAnswerSheet">交卷并离开
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="finishModal" role="dialog" tabindex="-1">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">考试结束</h5>
          </div>
          <div class="modal-body">
            考试已经结束，不能继续作答。请在5分钟内交卷
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" type="button" v-on:click="submitAnswerSheet">交卷并离开</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
    import moment from 'moment';
    import $ from 'jquery';
    import axios from 'axios';
    import "bootstrap/dist/js/bootstrap.js"
    import VueCountdown from '@chenfengyuan/vue-countdown';
    import 'bootstrap/dist/css/bootstrap.css'
    import * as common from '../../lib/commom.js'

    moment.locale('zh-cn');

    function showErrorMsg(msg) {
        alert(msg);
    }

    function handleResponse(res, cb) {
        switch (res.data.status) {
            case 200:
                cb(res);
                break;
            case 4001:
                showErrorMsg("考试不存在");
                break;
            case 4002:
                showErrorMsg("本场考试已被取消");
                break;
            case 4003:
                showErrorMsg("考试已经结束");
                break;
            case 4004:
                showErrorMsg("考试尚未开始");
                break;
            case 4005:
                showErrorMsg("已经交卷，无法继续作答");
                break;
        }
    }

    export default {
        components: {
            'countdown': VueCountdown
        },
        created: async function () {
            const examId = common.getUrlParams("id");
            if (examId == null) {
                showErrorMsg("考试参数错误，请检查链接是否正确");
                return
            }
            await axios.get(`/student/exam/${examId}`)
                .then(res => {
                    handleResponse(res, res => this.exam = res.data.data);
                });

            console.log(this.exam);
            this.backupKey = `exam-${this.exam.id}`;

            let backup;
            try {
                backup = JSON.parse(window.localStorage.getItem(this.backupKey));
            } catch (e) {
                console.log(e);
            }
            this.answerSheet.exam = this.exam.id;
            this.exam.testPaper.exerciseConfigs.forEach((e, i) => {
                let inputSQL;
                if (backup && backup.answers[i] && backup.answers[i].exerciseConfig === e.id) {
                    inputSQL = backup.answers[i].inputSQL
                } else {
                    inputSQL = null
                }
                this.answerSheet.answers.push({
                    exerciseConfig: e.id,
                    inputSQL: inputSQL
                });
            });

            this.exam.endTime = moment().unix() + 315;
            const remain = this.exam.endTime - moment().unix();
            this.time = remain * 1000;
            console.log(remain);
            $('#finishModal').modal({
                show: false,
                backdrop: 'static',
                keyboard: false
            });

            const showAlert = remain - 300;
            if (showAlert > 0) {
                setTimeout(() => {
                    $("#alert").removeClass("d-none").addClass("show");
                }, showAlert * 1000);
            }
        },
        data() {
            return {
                time: null,
                showIndex: 0,
                exam: {
                    testPaper: {
                        exerciseConfigs: []
                    }
                },
                answerSheet: {
                    exam: null,
                    answers: [],
                },
                backupKey: null,
            }
        },
        watch: {
            answerSheet: {
                handler() {
                    this.backup();
                },
                deep: true,
            },
        },
        methods: {
            onExamFinish() {
                console.log("exam finish");
                $("textarea").attr("readonly", "true");
                $("#finishModal").modal("show");
            },
            backup() {
                window.localStorage.setItem(this.backupKey, JSON.stringify(this.answerSheet));
            },
            submitAnswerSheet() {
                const answerSheet = JSON.parse(window.localStorage.getItem(this.backupKey));
                console.log(answerSheet);
                axios.post("/student/exam/answer_sheet", answerSheet)
                    .then(res => {
                        handleResponse(res, () => {
                            window.localStorage.removeItem(`exam-${this.exam.id}`);
                            window.location = "/view/student.html"
                        });
                    })
            },
            setPanel(n) {
                this.showIndex = n - 1;
            },
            nextPanel() {
                if (this.showIndex < this.exam.testPaper.exerciseConfigs.length - 1) {
                    this.showIndex += 1;
                } else {
                    $('#submitModal').modal("show");
                }
            },
            prevPanel() {
                if (this.showIndex > 0) {
                    this.showIndex -= 1;
                }
            },
            copySQL(index) {
                navigator.clipboard.writeText(this.exam.testPaper.exerciseConfigs[index].exercise.inputSQL).then(function () {
                    console.log('Async: Copying to clipboard was successful!');
                }, function (err) {
                    console.error('Async: Could not copy text: ', err);
                });
            }
        }
    }
</script>