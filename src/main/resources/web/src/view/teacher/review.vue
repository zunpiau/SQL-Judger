<template>
  <div>
    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
      <div class="sidebar-sticky">
        <nav class="nav nav-pills flex-column" id="nav_tabs">
          <a :class="['nav-link', (index === studentIndex ? 'active' : '')]" :href="'#student-' + index"
             role="tab"
             v-for="student, index in students" v-on:click.prevent="showStudent(student, index)">
            {{ student.number }}.{{ student.name }} / {{ getScore(student) }}
          </a>
        </nav>
      </div>
    </nav>
    <main class="col-md-9 ml-sm-auto col-lg-10 px-4 tab-content" role="main">
      <div class="w-50 float-left panel">
        <div :class="['tab-pane fade mr-3', (index === exerciseIndex ? 'show' : 'd-none')]" :id="'exercise-'+ index"
             role="tabpanel"
             v-for="exerciseConfig, index in exerciseConfigs">
          <div class="h3 d-flex justify-content-between">
            <span>{{ exerciseConfig.id + 1 }}. {{ exerciseConfig.exercise.title }}</span>
            <span class="badge badge-light">{{ exerciseConfig.score }}分</span>
          </div>
          <div>
            <span class="mt-1 font-weight-bold">SQL代码</span>
            <pre class="code">{{ exerciseConfig.exercise.expectedSQL }}</pre>
            <span class="mt-1 font-weight-bold">输出可视化</span>
            <ResultWrapper :result-wrapper="exerciseConfig.exercise.expectedData">

            </ResultWrapper>
            <span class="mt-1 font-weight-bold">原始输出</span>
            <pre class="code">{{ JSON.stringify(exerciseConfig.exercise.expectedData) }}</pre>
          </div>
          <div>
            <span class="mt-1 font-weight-bold">题目描述</span>
            <pre>{{ exerciseConfig.exercise.description }}</pre>
            <span class="mt-1 font-weight-bold">表结构</span>
            <pre class="code">{{ exerciseConfig.exercise.inputSQL }}</pre>
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
        </div>
      </div>
      <div class="w-50 float-right panel px-3">
        <div :class="['tab-pane fade', (index === studentIndex ? 'show' : 'd-none')]"
             :id="'student-'+ index" role="tabpanel" v-for="student, index in students">
          <div v-if="student.answers">
            <div :class="[(index === exerciseIndex ? 'show' : 'd-none')]" :id="'answer-'+ index"
                 v-for="answer, index in student.answers">
              <div v-if="answer">
                <div class="d-flex justify-content-end">
                  <div class="mb-2">
                    <label>得分：</label>
                    <input class="form-control form-control-sm d-inline-block score" type="number"
                           v-model="answer.score">
                  </div>
                </div>
                <div>
                  <span class="mt-1 font-weight-bold">学生输入</span>
                  <pre class="code">{{ answer.inputSQL }}</pre>
                  <span class="mt-1 font-weight-bold">输出可视化</span>
                  <ResultWrapper :result-wrapper="answer.inputData">

                  </ResultWrapper>
                  <span class="mt-1 font-weight-bold">原始输出</span>
                  <pre class="code">{{ JSON.stringify(answer.inputData) }}</pre>
                </div>
              </div>
              <div v-else>
                <p class="text-center">本题未作答</p>
              </div>
            </div>
          </div>
          <div v-else>
            <p class="text-center">该学生未参加考试</p>
          </div>
        </div>
        <div class="fixed-bottom fixed-right-bottom mb-3">
          <div class="d-flex justify-content-between bg-white">
            <button class="btn btn-secondary" v-on:click="prevPanel">上一题</button>
            <button :disabled="students[studentIndex].answers === null" class="btn btn-danger"
                    v-if="exerciseIndex === exerciseConfigs.length - 1" v-on:click="modifyScore">提交成绩
            </button>
            <button class="btn btn-primary" v-on:click="nextPanel">下一题</button>
          </div>
        </div>

      </div>
    </main>
  </div>
</template>
<style scoped> @import "./../../lib/sidebar.css"; </style>
<style scoped> @import "./../../lib/common.css"; </style>
<style>
  .fixed-right-bottom {
    padding-left: 60%;
    padding-right: 2%;
  }
</style>
<script>
    import "bootstrap/dist/js/bootstrap.js"
    import 'bootstrap/dist/css/bootstrap.css'
    import * as common from '../../lib/commom.js'
    import ResultWrapper from "../../components/ResultWrapper";
    import axios from 'axios';

    export default {
        components: {ResultWrapper},
        comments: {
            ResultWrapper
        },
        data() {
            return {
                exam: null,
                students: [],
                exerciseConfigs: [],
                exerciseIndex: 0,
                studentIndex: 0,
            }
        },
        async created() {
            const examId = common.getUrlParams("id");
            if (examId == null) {
                showErrorMsg("考试参数错误，请检查链接是否正确");
                return
            }
            this.exam = examId;
            await common.load2(`/teacher/exam/${examId}/exercise`, this.exerciseConfigs);
            await common.load2(`/teacher/exam/${examId}/student`, this.students);
        },
        methods: {
            getScore(student) {
                return student.answerSheet != null ? student.score : 'NaN';
            },
            showStudent(student, index) {
                this.studentIndex = index;
                this.exerciseIndex = 0;
            },
            prevPanel() {
                this.exerciseIndex = (this.exerciseIndex + this.exerciseConfigs.length - 1) % this.exerciseConfigs.length;
                if (this.exerciseIndex === this.exerciseConfigs.length - 1) {
                    this.studentIndex = (this.studentIndex + this.students.length - 1) % this.students.length;
                }
            },
            nextPanel() {
                this.exerciseIndex = (this.exerciseIndex + 1) % this.exerciseConfigs.length;
                if (this.exerciseIndex === 0) {
                    this.studentIndex = (this.studentIndex + 1) % this.students.length;
                }
            },
            modifyScore() {
                const student = this.students[this.studentIndex];
                const updateScoreDto = {
                    number: student.number,
                    answers: [],
                };
                student.answers.forEach(e => {
                    updateScoreDto.answers.push({
                        id: e.id,
                        score: e.score,
                    })
                });
                axios.put(`/teacher/answersheet/${student.answerSheet}/answer`, updateScoreDto)
                    .then(res => {
                        student.score = res.data.data;
                    })
                    .catch(reason => alert(reason));
            }
        }
    }
</script>