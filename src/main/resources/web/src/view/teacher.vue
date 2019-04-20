<template>
  <div class="container-fluid">
    <div class="row">
      <nav class="col-md-2 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
          <nav class="nav nav-pills flex-column" id="nav_tabs">
            <a class="nav-link active" data-toggle="pill" href="#home" role="tab">
              Dashboard
            </a>
            <a class="nav-link" data-toggle="pill" href="#exercise" role="tab" v-on:click.once="loadExercises">
              题目管理
            </a>
            <a class="nav-link" data-toggle="pill" href="#testPaper" role="tab" v-on:click.once="loadTestPaper">
              试卷管理
            </a>
            <a class="nav-link" data-toggle="pill" href="#exam" role="tab">
              考试管理
            </a>
          </nav>
        </div>
      </nav>

      <main class="col-md-9 ml-sm-auto col-lg-10 px-4 tab-content" role="main">
        <div class="tab-pane fade show active" id="home" role="tabpanel">
          <h2 class="h2 mb-3">Dashboard</h2>
          <div class="row">
            <div class="col-6">
              <div class="card border-primary shadow-sm">
                <div class="card-header">考试安排</div>
                <div class="card-body">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item text-danger">{{ this.countExams.progressing }} 项进行中</li>
                    <li class="list-group-item text-primary">{{ this.countExams.notStarted }} 项未开始</li>
                    <li class="list-group-item text-info">{{ this.countExams.finished }} 项已结束</li>
                    <li class="list-group-item text-muted">{{ this.countExams.cancel }} 项已取消</li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card border-info shadow-sm">
                <div class="card-header">教授班级</div>
                <div class="card-body text-info">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item" v-for="teaching in teachings">{{ teaching.clazz.grade }} {{
                      teaching.clazz.name }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane fade" id="exercise" role="tabpanel">
          <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
            <h2 class="h2 mb-3">题目列表</h2>
            <div class="d-flex justify-content-between align-items-center">
              <input class="search form-control mr-2" placeholder="搜索" v-model="filter.exercise">
              <div class="dropdown mr-2">
                <button class="btn btn-outline-secondary dropdown-toggle" data-toggle="dropdown" type="button">
                  题目类型
                </button>
                <div aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                  <div @click="filter.type = ''" class="dropdown-item">全部</div>
                  <div @click="filter.type = type" class="dropdown-item" v-for="type in exerciseTypes">{{ type }}</div>
                </div>
              </div>
              <button class="btn btn-outline-secondary mr-2" data-target="#importModal" data-toggle="modal">导入题目
              </button>
              <button class="btn btn-outline-primary" data-target="#addExerciseModal" data-toggle="modal">创建题目</button>
            </div>
          </div>
          <div class="modal fade full-screen" id="addExerciseModal" ref="addExerciseModal" role="dialog"
               tabindex="-1">
            <div class="modal-dialog modal-lg full-screen" role="document">
              <div class="modal-content full-screen">
                <div class="modal-header">
                  <h5 class="modal-title">创建题目</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <section class="mt-3">
                    <h3 class="h3">基本信息</h3>
                    <div class="form-group">
                      <label for="exerciseTitleInput">标题</label>
                      <input class="form-control" id="exerciseTitleInput" placeholder="标题" required
                             v-model="exercise.title">
                    </div>
                    <div class="form-group">
                      <label for="scoreInput">分数</label>
                      <input class="form-control" id="scoreInput" min="0" placeholder="分数" type="number"
                             v-model="exercise.score">
                    </div>
                    <div class="form-group">
                      <label>题目类型</label>
                      <div class="dropdown">
                        <button class="btn btn-outline-primary dropdown-toggle" data-toggle="dropdown" type="button">
                          {{ exercise.type === '' ? '请选择' : exercise.type}}
                        </button>
                        <div aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                          <a @click="exercise.type = type" class="dropdown-item" v-for="type in exerciseTypes">
                            {{ type }}</a>
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="describeInput">题目描述</label>
                      <textarea class="form-control" id="describeInput" required rows="3"
                                v-model="exercise.description"></textarea>
                    </div>
                  </section>
                  <section class="mt-5">
                    <h3 class="h3">输入数据</h3>
                    <div class="form-group">
                      <label for="inputSQLInput">表结构和初始数据</label>
                      <textarea class="form-control" id="inputSQLInput" placeholder="SQL语句，可为空" rows="3"
                                v-model="exercise.inputSQL"></textarea>
                      <div class="d-flex justify-content-end">
                        <button :disabled="disableRunSQL" class="btn btn-outline-secondary mt-1" id="inputBtn"
                                type="button"
                                v-on:click="getInputData">
                          生成输入数据
                        </button>
                      </div>
                    </div>
                    <div v-for="table in exercise.inputData">
                      <label>表格：{{ table.name }}</label>
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
                  </section>
                  <section class="mt-5">
                    <h3 class="h3">期望输出</h3>
                    <div class="form-group">
                      <textarea class="form-control" placeholder="SQL语句" rows="3"
                                v-model="exercise.expectedSQL"></textarea>
                      <div class="d-flex justify-content-end mt-1">
                        <button class="btn btn-outline-secondary" type="button" v-on:click="getexpectedData">生成期望输出
                        </button>
                      </div>
                    </div>
                    <textarea class="form-control" placeholder="期望输出预览" readonly
                              rows="3">{{ expectedDataJson }}</textarea>
                  </section>
                  <section class="d-flex justify-content-center mt-5 mb-5">
                    <button class="btn btn-outline-primary btn-lg w-25" v-on:click="addExercise">完成</button>
                  </section>
                </div>
              </div>
            </div>
          </div>
          <div class="modal fade" id="exerciseModal" role="dialog" tabindex="-1">
            <div class="modal-dialog modal-lg" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">题目详情</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <p class="h5">{{ selectExercise.title }}</p>
                  <pre>{{ selectExercise.description }}</pre>
                  <div v-if="selectExercise.inputSQL">
                    表结构
                    <pre class="code"><code>{{ selectExercise.inputSQL }}</code></pre>
                  </div>
                  <div v-if="selectExercise.inputData">
                    输入数据
                    <p class="code">{{ JSON.stringify(selectExercise.inputData) }}</p>
                  </div>
                  SQL代码
                  <pre class="code"><code>{{ selectExercise.expectedSQL }}</code></pre>
                  期望输出
                  <p class="code">{{ JSON.stringify(selectExercise.expectedData) }}</p>
                </div>
              </div>
            </div>
          </div>
          <div class="modal fade" id="importModal" ref="importModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">导入题目</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <div class="input-group">
                    <div class="custom-file">
                      <input
                          accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                          class="custom-file-input" type="file"
                          v-on:change="files = $event.target.files">
                      <label class="custom-file-label">{{ files.length === 0 ? '请选择 Excel 文件' : files[0].name }}</label>
                    </div>
                    <div class="input-group-append">
                      <button @click="uploadFile" class="btn btn-outline-secondary" type="button">上传</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">标题</th>
                <th scope="col">描述</th>
                <th scope="col">分数</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="exercise in filterExercises">
                <th scope="row">{{ exercise.id }}</th>
                <td class="exercise-title">{{ exercise.title }}</td>
                <td class="exercise-description">{{ exercise.description }}</td>
                <td>{{ exercise.score }}</td>
                <td class="exercise-operation">
                  <button class="btn btn-primary btn-sm mr-2" v-on:click="showExercise(exercise)">
                    详情
                  </button>
                  <button class="btn btn-danger btn-sm" v-if="deleteable(exercise)"
                          v-on:click="deleteExercise(exercise)">
                    删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="testPaper" role="tabpanel">
          <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">试卷列表</h3>
            <div class="d-flex justify-content-between align-items-center">
              <input class="search form-control mr-2" placeholder="搜索" v-model="filter.testPaper">
              <button class="btn btn-outline-primary" data-target="#createTestPaperModal" data-toggle="modal">创建试卷
              </button>
            </div>
          </div>
          <div class="card mb-5 shadow-sm" v-for="testPaper, index in filterTestPapers">
            <div class="card-header">
              <span class="h4">
                {{ testPaper.id }}.{{ testPaper.title }}
              </span>
            </div>
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <p class="card-text">总分: {{ testPaper.score }}</p>
                  <p class="card-text">题目: {{ testPaper.exerciseConfigs.length }}道</p>
                </div>
                <div class="d-flex align-items-center">
                  <button class="btn btn-primary" v-on:click="showTestPaper(testPaper)">查看</button>
                  <button class="btn btn-danger ml-2" v-if="!testPaper.used" v-on:click="deleteTestPaper(testPaper)">
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="modal fade full-screen" id="testPaperModal" ref="testPaperModal" role="dialog" tabindex="-1">
            <div class="modal-dialog modal-lg full-screen" role="document">
              <div class="modal-content full-screen">
                <div class="modal-header">
                  <h5 class="modal-title">查看试卷</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <div class="row">
                    <div class="col-6">
                      <div class="form-group">
                        <label for="testPaperTitleInput">标题</label>
                        <input class="form-control" id="testPaperTitleInput" placeholder="标题" required
                               v-model="selectTestPaper.title">
                      </div>
                      <div class="form-group">
                        <label for="testPaperScoreInput">总分</label>
                        <input class="form-control" disabled id="testPaperScoreInput" placeholder="总分" required
                               v-model="selectTestPaper.score">
                      </div>
                    </div>
                  </div>
                  <label>题目</label>
                  <div v-for="(exerciseConfig, index) in selectTestPaper.exerciseConfigs">
                    <div class="d-flex justify-content-between align-items-center">
                      <p class="h5">{{ index}}. {{ exerciseConfig.exercise.id }}-{{ exerciseConfig.exercise.title }}</p>
                      <div class="d-flex">
                        <input class="score form-control mr-2" type="number" v-model="exerciseConfig.score">
                        <button @click=" common.deleteElement(selectTestPaper.exerciseConfigs, exerciseConfig) "
                                class="btn btn-out-danger">
                          移除
                        </button>
                      </div>
                    </div>
                    <p>{{ exerciseConfig.exercise.description }}</p>
                    <pre class="code">{{ exerciseConfig.exercise.inputSQL }}</pre>
                    <pre class="code">{{ exerciseConfig.exercise.expectedSQL }}</pre>
                  </div>
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-dismiss="modal" type="button">关闭</button>
                  <button class="btn btn-primary" type="button" v-if="!selectTestPaper.used"
                          v-on:click="modifyTestPaper(selectTestPaper)">修改
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="modal fade" id="createTestPaperModal" ref="createTestPaperModal" role="dialog"
               tabindex="-1">
            <div class="modal-dialog modal-lg" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">创建试卷</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <div class="input-group mb-3">
                    <input class="form-control mr-2" placeholder="试卷名称" v-model="composeTestPaperRequest.title">
                  </div>
                  <label>选择题目类型/数量</label>
                  <ul>
                    <div v-for="(index, type) in composeTestPaperRequest.amounts">
                      <li>{{ type}}: {{ composeTestPaperRequest.amounts[type] }}道</li>
                    </div>
                  </ul>
                  <div class="input-group mb-3">
                    <div class="dropdown mr-2">
                      <button class="btn btn-outline-primary dropdown-toggle" data-toggle="dropdown" type="button">
                        {{ compose.exerciseType }}
                      </button>
                      <div aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                        <a @click="compose.exerciseType = type" class="dropdown-item" v-for="type in exerciseTypes">
                          {{ type }}</a>
                      </div>
                    </div>
                    <input class="mr-2" type="number" v-model="compose.amount">
                    <button @click="addExerciseTypeAmount()" class="btn btn-secondary mr-2">添加</button>
                  </div>
                </div>
                <div class="modal-footer">
                  <button @click="composeTestPaper" class="btn btn-primary">组卷</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane fade" id="exam" role="tabpanel">
          <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">考试列表</h3>
            <div class="d-flex justify-content-between align-items-center">
              <input class="search form-control mr-2" placeholder="搜索" v-model="filter.exam">
              <button @click.once="testPapers.length !== 0 || loadTestPaper()" class="btn btn-outline-primary"
                      data-target="#createExamModal" data-toggle="modal">创建考试
              </button>
            </div>
          </div>
          <div v-if="exams.length === 0">
            <p>当前没有考试</p>
          </div>
          <div class="card mb-5 shadow-sm" v-for="exam in filterExams">
            <div class="card-header">
              <span class="h4">{{ exam.id }}. {{ exam.title }}</span>
              <span class="badge badge-info ml-2">{{ getBadgeText(exam) }}</span>
            </div>
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <p class="card-text">考试时间: {{ unixEpochToString(exam.startTime) }} ~ {{
                    unixEpochToString(exam.endTime) }}</p>
                  <p class="card-text">考试班级: {{ exam.teaching.clazz.grade }} {{ exam.teaching.clazz.name }}</p>
                  <p class="card-text">试卷：{{ exam.testPaper.id }}.{{ exam.testPaper.title }}</p>
                  <p class="card-text" v-if="exam.answerAmount">答卷：{{ exam.answerAmount }}份</p>
                </div>
                <div class="d-flex align-items-center">
                  <button class="btn btn-danger" v-if="cancelable(exam)" v-on:click="cancelExam(exam)">取消
                  </button>
                  <button class="btn btn-primary" v-if="correctable(exam)" v-on:click="correctExam(exam, $event)">改卷
                  </button>
                  <button class="btn btn-secondary mr-2" v-if="publicable(exam)" v-on:click="publicScore(exam)">
                    公布成绩
                  </button>
                  <a class="btn btn-primary mr-2" v-bind:download="filename(exam)"
                     v-bind:href="`/teacher/exam/${exam.id}/score/export`"
                     v-if="exam.status === 4">
                    导出成绩
                  </a>
                  <a class=" btn btn-secondary" target="_blank" v-bind:href="'/view/teacher/review.html?id=' + exam.id"
                     v-if="reviewable(exam)">复查
                  </a>
                </div>
              </div>
            </div>
          </div>
          <div class="modal fade" id="createExamModal" ref="createExamModal" role="dialog"
               tabindex="-1">
            <div class="modal-dialog modal-lg" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">创建考试</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <section>
                    <div class="form-group">
                      <label for="examTitleInput">标题</label>
                      <input class="form-control" id="examTitleInput" placeholder="标题" required v-model="exam.title">
                    </div>
                    <div class="form-group">
                      <label>试卷</label>
                      <div class="dropdown">
                        <button class="btn btn-outline-primary dropdown-toggle" data-toggle="dropdown" type="button">
                          {{ exam.testPaper.title ? exam.testPaper.title : '请选择' }}
                        </button>
                        <div aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                          <a @click="exam.testPaper = type" class="dropdown-item" v-for="type in testPapers">
                            {{ type.title }}</a>
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>选择班级</label>
                      <div class="form-check" v-for="teaching in teachings">
                        <input class="form-check-input" type="checkbox" v-bind:value="teaching.clazz.id"
                               v-model="checkedClazzs">
                        <label class="form-check-label">
                          {{ teaching.clazz.grade }} {{ teaching.clazz.name }}
                        </label>
                      </div>
                    </div>
                    <div class="form-group form-inline">
                      <label class="pr-2">考试开始时间</label>
                      <date-picker :config="configs.start" @dp-change="onStartTimeChange" v-model="startTime">
                      </date-picker>
                    </div>
                    <div class="form-group form-inline">
                      <label class="pr-2">考试结束时间</label>
                      <date-picker :config="configs.end" @dp-change="onEndTimeChange" v-model="endTime"></date-picker>
                    </div>
                  </section>
                  <section class="d-flex justify-content-center mt-5 mb-5">
                    <button class="btn btn-outline-primary btn-lg w-25" v-on:click="addExam">完成</button>
                  </section>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>
<style scoped> @import "./../lib/sidebar.css"; </style>
<style scoped> @import "./../lib/common.css"; </style>
<style scoped> @import "./../lib/icons.css"; </style>
<style>
  .exercise-description {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 50ch;
  }

  .exercise-title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 20ch;
  }

  .exercise-operation {
    width: 8.5rem;
  }

  .btn-outline-primary.dropdown-toggle {
    width: 10rem;
    text-align: left;
    color: unset !important;
    border-color: #ced4da;
  }

  .show > .btn-outline-primary.dropdown-toggle,
  .btn-outline-primary.dropdown-toggle:hover,
  .btn-outline-primary.dropdown-toggle:active {
    background-color: unset !important;
  }

  input.search {
    width: 8rem;
  }
</style>
<script>
    import axios from 'axios';
    import moment from 'moment';
    import Vue from 'vue'
    import $ from 'jquery';
    import "bootstrap/dist/js/bootstrap.js"
    import datePicker from 'vue-bootstrap-datetimepicker';
    import * as common from '../lib/commom.js'

    import 'bootstrap/dist/css/bootstrap.css'
    import 'pc-bootstrap4-datetimepicker/build/css/bootstrap-datetimepicker.css';

    moment.locale('zh-cn');
    const app = {
        name: 'app',
        components: {
            datePicker
        },
        data() {
            return {
                composeTestPaperRequest: {
                    title: null,
                    score: 0,
                    amounts: {}
                },

                files: [],
                exerciseTypes: [
                    "简单查询", "多表查询", "条件查询", "创建表", "修改表", "INSERT", "DELETE", "UPDATE"
                ],
                compose: {
                    exerciseType: "简单查询",
                    amount: 0,
                },
                configs: {
                    start: {
                        useCurrent: false,
                        minDate: moment(),
                        maxDate: false,
                    },
                    end: {
                        useCurrent: false,
                        minDate: moment(),
                    }
                },
                startTime: null,
                endTime: null,
                currentTeacher: null,
                exercises: [],
                exercise: {
                    title: "",
                    type: "",
                    description: "",
                    score: "10",
                    inputSQL: "",
                    inputData: null,
                    expectedSQL: "",
                    expectedData: null,
                },
                testPapers: [],
                testPaper: {
                    title: null,
                    selectExercises: [],
                },
                teachings: [],
                exam: {
                    title: null,
                    startTime: null,
                    teaching: {
                        clazz: {},
                        teacher: {}
                    },
                    endTime: null,
                    testPaper: {
                        id: null,
                        title: null,
                    },
                },
                exams: [],
                countExams: {
                    cancel: 0,
                    finished: 0,
                    progressing: 0,
                    notStarted: 0,
                },
                checkedClazzs: [],
                selectExercise: {},
                selectTestPaper: {},
                total: 0,
                filter: {
                    exam: '',
                    testPaper: '',
                    exercise: '',
                    type: '',
                },
                common: common,
            }
        },
        async created() {
            await this.loadTeachings();
            await this.loadExams();
            await axios.get("/teacher/profile")
                .then(res => {
                    if (res.data.status === 200) {
                        this.currentTeacher = res.data.data;
                    } else {
                        console.log(res.data);
                        window.location.reload();
                    }
                });
        },
        watch: {
            exams() {
                this.classifyExams();
            },
            testPaper: {
                handler() {
                    let t = 0;
                    this.testPaper.selectExercises.forEach(e => t += Number.parseInt(e.score));
                    this.total = t;
                },
                deep: true,
            },
            selectTestPaper: {
                handler() {
                    let t = 0;
                    this.selectTestPaper.exerciseConfigs.forEach(e => t += Number.parseInt(e.score));
                    this.selectTestPaper.score = t;
                },
                deep: true,
            }
        },
        computed: {
            expectedDataJson() {
                if (this.exercise.expectedData != null)
                    return JSON.stringify(this.exercise.expectedData);
                else
                    return "";
            },
            disableRunSQL() {
                return this.exercise.inputSQL === "";
            },
            filterExams() {
                return this.exams.filter(e => this.filter.exam === '' || e.title.indexOf(this.filter.exam) !== -1);
            },
            filterTestPapers() {
                return this.testPapers.filter(e => this.filter.testPaper === '' || e.title.indexOf(this.filter.testPaper) !== -1);
            },
            filterExercises() {
                return this.exercises.filter(e => (this.filter.exercise === '' || e.title.indexOf(this.filter.exercise) !== -1) &&
                    (this.filter.type === '' || e.type === this.filter.type));
            }
        },
        methods: {
            addExerciseTypeAmount() {
                Vue.set(this.composeTestPaperRequest.amounts, this.compose.exerciseType, this.compose.amount);
            },
            filename(exam) {
                const date = moment.unix(exam.startTime).format("MMMDo");
                return `${date}-${exam.title}-${exam.teaching.clazz.name}.xls`
            },
            onStartTimeChange(e) {
                console.log(e.date.unix());
                this.exam.startTime = e.date.unix();
                console.log(this.exam.startTime);
                this.$set(this.configs.end, 'minDate', e.date || null);
            },
            onEndTimeChange(e) {
                this.exam.endTime = e.date.unix();
                this.$set(this.configs.start, 'maxDate', e.date || null);
            },
            classifyExams() {
                let cancel = 0, finished = 0, progressing = 0, notStarted = 0;
                this.exams.forEach(exam => {
                    if (exam.status === 1) {
                        cancel++;
                        return
                    }
                    if (moment().isBefore(exam.startTime * 1000)) {
                        notStarted++;
                        return
                    }
                    if (moment().isAfter(exam.endTime * 1000)) {
                        finished++;
                        return
                    }
                    progressing++;
                });
                this.countExams.cancel = cancel;
                this.countExams.finished = finished;
                this.countExams.progressing = progressing;
                this.countExams.notStarted = notStarted;
            },
            deleteable(exercise) {
                return exercise.teacher.number === this.currentTeacher.number;
            },
            cancelable(exam) {
                if (exam.status === 1) return false;
                return moment().isBefore(exam.startTime * 1000);
            },
            correctable(exam) {
                return exam.status === 0 && moment().isAfter(exam.endTime * 1000);
            },
            correctExam(exam, event) {
                axios.put(`/teacher/exam/${exam.id}/correct`)
                    .then(res => {
                        if (res.status === 200) {
                            $(event.target).attr("disabled", "true");
                        }
                    })
            },
            reviewable(exam) {
                return exam.status >= 3 && moment().isAfter(exam.endTime * 1000);
            },
            publicable(exam) {
                return exam.status === 3 && moment().isAfter(exam.endTime * 1000);
            },
            unixEpochToString(second) {
                return moment.unix(second).format("YYYY-MM-DD HH:mm");
            },
            getBadgeText(exam) {
                switch (exam.status) {
                    case 1:
                        return "已取消";
                    case 2:
                        return "改卷中";
                    case 3:
                        return "已改卷";
                    case 4:
                        return "已复查";
                }
                const now = moment();
                if (now.isBefore(exam.startTime * 1000)) {
                    return "未开始";
                }
                if (now.isAfter(exam.endTime * 1000)) {
                    return "已结束";
                }
                return "进行中";
            },
            getInputData() {
                axios.post("/teacher/sql/retrieve", this.exercise.inputSQL, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then((response) => {
                    console.log(response.data);
                    if (response.data.status === 200) {
                        this.exercise.inputData = response.data.data;
                    } else {
                        alert(response.data.msg);
                    }
                })
                    .catch(reason => {
                        console.log(reason);
                    })
            },
            getexpectedData() {
                axios.post("/teacher/sql/excute", {
                    schema: this.exercise.inputSQL,
                    excute: this.exercise.expectedSQL
                }, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then((response) => {
                    console.log(response.data);
                    if (response.data.status === 200) {
                        this.exercise.expectedData = response.data.data;
                    } else {
                        alert(response.data.msg);
                    }
                }).catch(reason => console.log(reason))
            },
            showExercise(exercise) {
                this.selectExercise = exercise;
                $('#exerciseModal').modal('show');
            },
            loadExercises() {
                common.load2("/teacher/exercise", this.exercises);
            },
            deleteExercise(exercise) {
                axios.delete(`/teacher/exercise/${exercise.id}`)
                    .then(res => {
                        if (res.data.status === 200 && res.data.data) {
                            common.deleteElement(this.exercises, exercise);
                            common.deleteElement(this.testPaper.selectExercises, exercise);
                        } else if (res.data.status === 400) {
                            alert("该题目已被引用，无法删除");
                        }
                    })
            },
            addExercise() {
                console.log(this.exercise);
                if (this.exercise.inputSQL && !this.exercise.inputData) {
                    alert("请生成输入数据预览");
                    return
                }
                if (this.exercise.expectedSQL && !this.exercise.expectedData) {
                    alert("请生成期望输出预览");
                    return
                }
                if (this.exercise.type === '' || !this.exercise.title || !this.exercise.description) {
                    alert("请输入基本信息");
                    return
                }
                axios.post("/teacher/exercise", this.exercise)
                    .then((response) => {
                        console.log(response.data);
                        if (response.data.status === 200) {
                            this.exercises.push(response.data.data);
                            alert("已添加");
                        } else {
                            alert(response.data.msg);
                        }
                    }).catch(reason => console.log(reason))
            },
            loadTeachings() {
                common.load2("/teacher/teaching", this.teachings);
            },
            loadTestPaper() {
                common.load2("/teacher/testPaper", this.testPapers);
            },
            showTestPaper(testPaper) {
                this.selectTestPaper = JSON.parse(JSON.stringify(testPaper));
                $('#testPaperModal').modal('show');
            },
            deleteTestPaper(testpaper) {
                axios.delete(`/teacher/testPaper/${testpaper.id}`)
                    .then(res => {
                        if (res.data.status === 200 && res.data.data) {
                            common.deleteElement(this.testPapers, testpaper);
                        } else if (res.data.status === 400) {
                            alert("该试卷已被引用，无法删除");
                        }
                    })
            },
            addTestPaper() {
                const testPaperVo = {
                    title: this.testPaper.title,
                    exerciseConfigs: []
                };
                this.testPaper.selectExercises.forEach(e => {
                    testPaperVo.exerciseConfigs.push({
                        exercise: e.id,
                        score: e.score,
                    })
                });
                axios.post("/teacher/testPaper", testPaperVo)
                    .then(res => {
                        $(this.$refs.createTestPaperModal).modal('hide');
                        this.testPapers.push(res.data.data);
                    })
                    .catch(res => {
                        console.log(res);
                    })
            },
            addExam() {
                console.log(this.exam);
                if (!this.exam.title) {
                    alert("请输入标题");
                    return
                }
                if (!this.exam.testPaper.id) {
                    alert("请选择试卷");
                    return
                }
                if (this.checkedClazzs.length === 0) {
                    alert("请选择至少选择一个班级");
                    return
                }
                if (!this.exam.startTime) {
                    alert("请设置考试开始时间");
                    return
                }
                if (!this.exam.endTime) {
                    alert("请设置考试结束时间");
                    return
                }
                this.checkedClazzs.forEach(clazz => {
                    this.exam.teaching.clazz.id = clazz;
                    this.exam.testPaper = {id: this.exam.testPaper.id};
                    axios.post("/teacher/exam", this.exam)
                        .then(res => {
                            console.log(res.data);
                            if (res.data.status === 200) {
                                $(this.$refs.createExamModal).modal('hide');
                                this.exams.push(res.data.data);
                            }
                        })
                        .catch(reason => console.log(reason));
                })
            },
            loadExams() {
                common.load2("/teacher/exam", this.exams);
            },
            cancelExam(exam) {
                axios.put(`/teacher/exam/${exam.id}/cancel`)
                    .then(res => {
                        console.log(res);
                        exam.status = 1;
                        this.classifyExams();
                    })
                    .catch(reason => console.log(reason));
            },
            publicScore(exam) {
                axios.put(`/teacher/exam/${exam.id}/public`)
                    .then(res => {
                        console.log(res);
                        exam.status = 4;
                        this.classifyExams();
                    })
                    .catch(reason => console.log(reason));
            },
            uploadFile() {
                if (this.files.length === 0) {
                    return
                }
                const formData = new FormData();
                formData.append('file', this.files[0]);
                axios.post("/teacher/exercise/import", formData)
                    .then(res => {
                        if (res.data.status === 200) {
                            alert(`上传了${res.data.data}条记录，正在导入系统中`);
                            $(this.$refs.importModal).modal('hide')
                        } else {
                            alert("文件格式有误")
                        }
                    })
                    .catch(reason => console.log(reason));
            },
            composeTestPaper() {
                axios.post("/teacher/testPaper/compose", this.composeTestPaperRequest)
                    .then(res => {
                        this.testPapers.push(res.data.data);
                        $(this.$refs.createTestPaperModal).modal("hide");
                    })
            },
            modifyTestPaper(testPaper) {
                const testPaperDTO = {
                    title: testPaper.title,
                    id: testPaper.id,
                    score: testPaper.score,
                    exerciseConfigs: [],
                }
                testPaper.exerciseConfigs.forEach(e => {
                    testPaperDTO.exerciseConfigs.push({
                        exercise: e.exercise.id,
                        score: e.score,
                    })
                });
                axios.put("/teacher/testPaper", testPaperDTO)
                    .then(res => {
                        $(this.$refs.testPaperModal).modal('hide');
                        let del;
                        this.testPapers.forEach(t => {
                            if (t.id === testPaperDTO.id)
                                del = t;
                        })
                        common.deleteElement(this.testPapers, del);
                        this.testPapers.push(res.data.data);
                    })
                    .catch(res => {
                        console.log(res);
                    })
            },
        }
    };
    export default app;
</script>