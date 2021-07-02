<template>
<div>
  
  <v-container>
  <v-row >
    <v-col cols="4">
    <v-card elevation="5"
              tile
              height="100%"
              >
        <v-card-title>기본 정보<v-spacer></v-spacer>
         </v-card-title>
            <v-card-text >
              <v-container>
                <v-row>
                  <v-col
                    cols="9"
                    sm="6"
                    md="6"
                  >
                    <v-text-field
                      label="성명"
                      v-model="user.name"
                      disabled
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="6"
                    sm="6"
                    md="6"
                  >
                    <v-text-field
                      label="사번"
                      v-model="user.compNo"
                      disabled
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="6"
                    sm="6"
                    md="6"
                  >
                    <v-text-field
                      label="부서"
                      v-model="user.deptName"
                      disabled
                    ></v-text-field>
                  </v-col>
                  <v-col
                     cols="6"
                     sm="6"
                     md="6"
                    >
                      <v-text-field
                        label="입사일"
                        v-model="user.joinDate"
                        disabled
                      ></v-text-field>
                  </v-col>
                  <v-col
                        cols="15"
                        sm="6"
                        md="4"
                      >
                        <v-text-field
                          label="발생연차(일)"
                          v-model="user.pto.all"
                          disabled
                        ></v-text-field>
                    </v-col>
                  <v-col
                      cols="15"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        label="사용연차(일)"
                        v-model="user.pto.used"
                        disabled
                      ></v-text-field>
                  </v-col>
                  <v-col
                      cols="15"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        label="잔여연차(일)"
                        v-model="user.pto.unused"
                        disabled
                      ></v-text-field>
                  </v-col>

                </v-row>
              </v-container>

            </v-card-text>
          </v-card>
    </v-col>

    <v-col cols="4">
        <v-card elevation="5"
              height="100%"
                tile>
            <v-card-title>휴가 신청 <v-btn
                         color="#999999"
                         text
                         @click="calendarDialog = true"
                       >
                         <v-icon>mdi-calendar-search</v-icon>일정보기
                       </v-btn>
            <v-spacer></v-spacer>
            <v-btn
                                     color="#1f202d darken-1"
                                     text
                                     @click="dialog = false"
                                   >
                                     <v-icon>mdi-calendar-import</v-icon> 휴가 신청
                                   </v-btn>            
            </v-card-title>
            
            <v-container>
            <v-row>
            <v-col cols="12">
                <date-picker :range="true" paramLabel="*연차 시작,종료일을 선택해주세요."></date-picker>
              </v-col>
              <v-col cols="6"
                    sm="6"
                    md="6">
                <v-select
                  :items="['오전 반차', '오후 반차', '연차']"
                  label="*휴가 구분"
                  required
                ></v-select>
              </v-col>
              
              <v-col cols="12">
              <v-textarea
                        label="휴가 사유"
                        value="개인 사유"
                        hint="간략한 휴가 사유를 작성해주세요."
                        rows="1"
                      ></v-textarea>
              </v-col>
              </v-row>
            </v-container>
        </v-card>
    </v-col>
    <v-col cols="4">
            <v-card elevation="5"
              height="100%"
                    tile>
                <v-card-title>휴가 취소 <v-spacer></v-spacer>
                <v-btn
                                     color="#BC544B"
                                     text
                                     @click="testAxios"
                                   >
                                     <v-icon>mdi-calendar-remove-outline</v-icon> 취소신청
                                   </v-btn>
                </v-card-title>
                <v-col>
                <v-data-table
                                    v-model="selectedCancelPtos"
                                    :headers="cancelHeaders"
                                    :items="ptoLists"
                                    item-key="id"
                                    show-select
                                    hide-default-footer
                                    class="elevation-2 overflow-y-auto"                                
                                    fixed-header
                                    height="200px"
                                  >
                </v-data-table>
                </v-col>
                <v-col>
                  <v-textarea
                      label="취소 사유"
                      value=""
                      placeholder="취소사유를 입력해주세요."
                      rows="2"
                    ></v-textarea>
                </v-col>
            </v-card>
    </v-col>
  </v-row>
  </v-container>
  <!-- 하단 휴가 신청 내역 데이터 그리드 영역 -->
  <v-container height="60%">
  <v-card>
                  <v-card-title>
                    휴가 신청 내역
                    <v-spacer></v-spacer>
                   </v-card-title>
    <v-row>
        <v-col cols="3">
            <date-picker paramLabel="시작일" :paramDate="(new Date(new Date().getFullYear(),0,2)).toISOString().substr(0, 10)"></date-picker>
        </v-col>
        <v-col cols="3">
            <date-picker paramLabel="종료일"></date-picker>
        </v-col>
        <v-col cols="3">
            <v-select
              :items="['오전 반차', '오후 반차', '연차']"
              label="*휴가 구분"
              required
            ></v-select>

        </v-col>
        <v-col cols="3" class="text-right">
            <v-btn
                color="#1f202d"
                class="ma-4 white--text"
                >
                검 색
            </v-btn>
        </v-col>
    </v-row>

    <v-row>
        <v-col>
            <v-data-table
                v-model="selectedCancelPtos"
                :headers="cancelHeaders"
                :items="ptoLists"
                item-key="id"
                show-select
                class="elevation-1"
              >
            </v-data-table>
        </v-col>
    </v-row>
    </v-card>
  </v-container>

  <!-- 일정 확인 캘린더 영역 -->
    <v-dialog
        transition="dialog-top-transition"
        v-model="calendarDialog"
    >

    <v-card>
        <v-col>
                  <v-sheet height="64">

                    <v-toolbar
                      flat
                    >

                      <v-btn
                        outlined
                        class="mr-4"
                        color="grey darken-2"
                        @click="setToday"
                      >
                        Today
                      </v-btn>
                      <v-menu
                          bottom
                          right
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-btn
                              outlined
                              color="grey darken-2"
                              v-bind="attrs"
                              v-on="on"
                            >
                              <span>{{ typeToLabel[type] }}</span>
                              <v-icon right>
                                mdi-menu-down
                              </v-icon>
                            </v-btn>
                          </template>
                          <v-list>
                            <v-list-item @click="type = 'day'">
                              <v-list-item-title>Day</v-list-item-title>
                            </v-list-item>
                            <v-list-item @click="type = 'week'">
                              <v-list-item-title>Week</v-list-item-title>
                            </v-list-item>
                            <v-list-item @click="type = 'month'">
                              <v-list-item-title>Month</v-list-item-title>
                            </v-list-item>
                          </v-list>
                        </v-menu>
                      <v-btn
                        fab
                        text
                        small
                        color="grey darken-2"
                        @click="prev"
                      >
                        <v-icon small>
                          mdi-chevron-left
                        </v-icon>
                      </v-btn>
                      <v-btn
                        fab
                        text
                        small
                        color="grey darken-2"
                        @click="next"
                      >
                        <v-icon small>
                          mdi-chevron-right
                        </v-icon>
                      </v-btn>
                      <v-toolbar-title v-if="$refs.calendar">
                        {{ $refs.calendar.title }}
                      </v-toolbar-title>
                      <v-spacer></v-spacer>


                      <v-btn
                        outlined
                        class="mr-4"
                        color="grey darken-2"
                        @click="calendarDialog = false"
                      >
                        <v-icon>mdi-window-close</v-icon>
                      </v-btn>

                    </v-toolbar>
                  </v-sheet>
                  <v-sheet height="500">
                    <v-calendar
                      ref="calendar"
                      v-model="focus"
                      color="primary"
                      :events="events"
                      :event-color="getEventColor"
                      :type="type"
                      @click:event="showEvent"
                      @click:more="viewDay"
                      @click:date="viewDay"
                      @change="updateRange"
                    ></v-calendar>
                    <v-menu
                      v-model="selectedOpen"
                      :close-on-content-click="false"
                      :activator="selectedElement"
                      offset-x
                    >
                      <v-card
                        color="grey lighten-4"
                        min-width="350px"
                        flat
                      >
                        <v-toolbar
                          :color="selectedEvent.color"
                          dark
                        >
                          <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                          </v-btn>
                          <v-toolbar-title v-html="selectedEvent.name"></v-toolbar-title>
                          <v-spacer></v-spacer>
                          <v-btn icon>
                            <v-icon>mdi-heart</v-icon>
                          </v-btn>
                          <v-btn icon>
                            <v-icon>mdi-dots-vertical</v-icon>
                          </v-btn>
                        </v-toolbar>
                        <v-card-text>
                          <span v-html="selectedEvent.details"></span>
                        </v-card-text>
                        <v-card-actions>
                          <v-btn
                            text
                            color="secondary"
                            @click="selectedOpen = false"
                          >
                            Cancel
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-menu>
                  </v-sheet>
                </v-col>
    </v-card>
    </v-dialog>
</div>
</template>

<script>
import DatePicker from '@/components/calendar/DatePicker'

export default {
  name: 'PaidTimeOff',
  components: {DatePicker},
  data: () => ({
        // 일정관리 캘린더
        focus: '',
        type: 'month',
        typeToLabel: {
          month: 'Month',
          week: 'Week',
          day: 'Day',
          '4day': '4 Days',
        },
        selectedEvent: {},
        selectedElement: null,
        selectedOpen: false,
        events: [],
        colors: ['deep-purple', 'grey darken-1', 'orange'],
        names: ['연차', '오후반차', '오전반차'],
        calendarDialog: false,

        // 사용자 기본정보 더미
        user: {
            name: '홍길동',
            compNo: 'K0001',
            deptName: '인트라플랫폼팀',
            role: '팀원',
            joinDate: '2020.01.01',
            pto: {
                days: '3',
                type: '연차',
                all: 15,
                used: 3,
                unused: 12
            }
        },

        // 휴가 취소 & 더미
        selectedCancelPtos: [],
        cancelHeaders: [
          {
            text: '',
            align: 'start',
            sortable: false,
          },
          { text: '시작일', value: 'start' },
          { text: '종료일', value: 'end' },
          { text: '일수', value: 'days' },
          { text: '휴가구분', value: 'ptoType' },
        ],
        ptoLists: [
          {
            id: 1,
            start: '2021-06-01',
            end: '2021-06-03',
            days: 3,
            ptoType: '연차'
          },
          {
            id: 2,
            start: '2021-06-11',
            end: '2021-06-11',
            days: 0.5,
            ptoType: '오전 반차'
          },
          {
            id: 3,
            start: '2021-06-01',
            end: '2021-06-03',
            days: 3,
            ptoType: '연차'
          },
          {
            id: 4,
            start: '2021-06-11',
            end: '2021-06-11',
            days: 0.5,
            ptoType: '오전 반차'
          },
          {
            id: 5,
            start: '2021-06-01',
            end: '2021-06-03',
            days: 3,
            ptoType: '연차'
          },
          {
            id: 6,
            start: '2021-06-11',
            end: '2021-06-11',
            days: 0.5,
            ptoType: '오전 반차'
          },
        ],
      }),
      mounted () {
        this.loadData()
        if(this.$refs.calendar) this.$refs.calendar.checkChange()
      },
      methods: {
        loadData () {
          this.getUserInfo()
        },
        viewDay ({ date }) {
          this.focus = date
          this.type = 'day'
        },
        getEventColor (event) {
          return event.color
        },
        setToday () {
          this.focus = ''
        },
        prev () {
          this.$refs.calendar.prev()
        },
        next () {
          this.$refs.calendar.next()
        },
        showEvent ({ nativeEvent, event }) {    // 일정 이벤트 관리
          const open = () => {
            this.selectedEvent = event
            this.selectedElement = nativeEvent.target
            requestAnimationFrame(() => requestAnimationFrame(() => this.selectedOpen = true))
          }

          if (this.selectedOpen) {
            this.selectedOpen = false
            requestAnimationFrame(() => requestAnimationFrame(() => open()))
          } else {
            open()
          }

          nativeEvent.stopPropagation()
        },
        updateRange ({ start, end }) {  // 일정 이벤트 갱신
          const events = []

          let tempData = [{type:'tmo',end:'2021-06-03',start:'2021-06-03',details:'test',title:'홍길동 연차'},
          {type:'tdo',end:'2021-06-04',start:'2021-06-02',details:'연차',title:'김호남 연차'},
          {type:"tao",end:'2021-06-27',start:'2021-06-27',details:'오후반차',title:'홍길동 오후 반차'},
          {type:"tmo",end:'2021-06-28',start:'2021-06-28',details:'오전 반차',title:'김호남 오전 반차'},
          {type:"tao",end:'2021-06-27',start:'2021-06-27',details:'오후반차',title:'홍길동 오후 반차'},
          {type:"tmo",end:'2021-06-28',start:'2021-06-28',details:'오전 반차',title:'김호남 오전 반차'},
          {type:"tdo",end:'2021-06-27',start:'2021-06-27',details:'연차',title:'홍길동 오후 반차'},
          {type:"tdo",end:'2021-06-28',start:'2021-06-28',details:'연차',title:'김호남 오전 반차'}]
          const eventCount = tempData.length
          console.log(start)
          console.log(end)

          for (let i = 0; i < eventCount; i++) {
            events.push({
              name: tempData[i].title,
              start: new Date(tempData[i].start),
              end: new Date(tempData[i].end),
              color: tempData[i].type === 'tdo' ? this.colors[0] : (tempData[i].type === 'tao' ? this.colors[1] : this.colors[2]),
              timed: false,
            })
          }

          this.events = events
        },
        rnd (a, b) {
          return Math.floor((b - a + 1) * Math.random()) + a
        },
        testAxios() {            
            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.GET_PTO_INFO.replace('{id}',userId)
            this.axios.get(apiUrl).then((response) => {
              console.log(response.data)
            })
        },
        async getUserInfo () {
            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.GET_PTO_INFO.replace('{id}',userId)
            await this.axios.get(apiUrl).then((response) => {
              console.log(response.data)
              /*user: {
                  name: '홍길동',
                  compNo: 'K0001',
                  deptName: '인트라플랫폼팀',
                  role: '팀원',
                  joinDate: '2020.01.01',
                  pto: {
                      days: '3',
                      type: '연차',
                      all: 15,
                      used: 3,
                      unused: 12
                  }
              },*/
            })
        }
      },

};
</script>
