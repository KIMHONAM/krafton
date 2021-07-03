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
                        v-model="user.pto.useDays"
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
                        v-model="user.pto.unusedDays"
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
                         @click="showCalendar"
                       >
                         <v-icon>mdi-calendar-search</v-icon>일정보기
                       </v-btn>
            <v-spacer></v-spacer>
            <v-btn
                                     color="#1f202d darken-1"
                                     text
                                     @click="postPTO"
                                     :disabled="endApplication"
                                   >
                                     <v-icon>mdi-calendar-import</v-icon> 휴가 신청
                                   </v-btn>            
            </v-card-title>
            
            <v-container>
            <v-row>
            <v-col cols="12">
                <date-picker :range="true" paramLabel="*연차 시작,종료일을 선택해주세요." @updateDate="(d) => (applicateDates = d)"></date-picker>
              </v-col>
              <v-col cols="6"
                    sm="6"
                    md="6">
                <v-select
                  v-model="selectedPtoType"
                  :items="ptoTypes"
                  label="*휴가 구분"
                  item-text="name"
                  item-value="value"
                  return-object
                  required
                ></v-select>
              </v-col>
              
              <v-col cols="12">
              <v-textarea
                        v-model="applicateReason"
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
                                     :disabled="endCancelRequest"
                                     @click="putCancelPTO"
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
                <template v-slot:no-data>
                  <v-alert :value="true" icon="info">
                    취소 가능 휴가 내역이 없습니다.
                  </v-alert>
                </template>
                </v-data-table>
                </v-col>
                <v-col>
                  <v-textarea
                      label="*취소 사유"
                      value=""
                      placeholder="취소사유를 입력해주세요."
                      v-model="cancelReason"
                      rows="2"
                    ></v-textarea>
                </v-col>
            </v-card>
    </v-col>
  </v-row>
  </v-container>
  <!-- 하단 휴가 신청 내역 데이터 그리드 영역 -->
  <v-container height="60%">
  <v-card >
                  <v-card-title>
                    휴가 신청 내역
                    <v-spacer></v-spacer>
                   </v-card-title>
    
    <v-container>
    <v-row>
        <v-col cols="3">
            <date-picker paramLabel="시작일" :paramDate="(new Date(new Date().getFullYear(),0,2)).toISOString().substr(0, 10)" @updateDate="(d) => (searchStartDate = d)" ></date-picker>
        </v-col>
        <v-col cols="3">
            <date-picker paramLabel="종료일" :paramDate="this.$moment(new Date()).add(1, 'M').format('YYYY-MM-DD')" @updateDate="(d) => (searchEndDate = d)"></date-picker>
        </v-col>
        <v-col cols="3">
            <v-select
                  v-model="defaultPtoHistSelect"
                  :items="searchPtoType"
                  label="*휴가 구분"
                  item-text="name"
                  item-value="value"
                  return-object
                  required
                ></v-select>
        </v-col>
        <v-col cols="3" class="text-right">
            <v-btn
                color="#1f202d"
                class="ma-4 white--text"
                @click="getPTOHistories"
                >
                검 색
            </v-btn>
        </v-col>
    </v-row>

    <v-row>
        <v-col>
            <v-data-table
                :headers="totalPtoHeaders"
                :items="ptoHistList"            
                :footer-props="footerProps" 
                :loading="loading"
                :server-items-length="ptoHistLenth"
                :options.sync="ptoHistOptions"
                v-model="ptoHistList"
                class="elevation-1"
                height="300px"
              >
              <template v-slot:no-data>
                  <v-alert :value="true" icon="info">
                    휴가 신청 내역이 없습니다.
                  </v-alert>
                </template>
            </v-data-table>
        </v-col>
    </v-row>
    </v-container>
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
        calendarItems: [],
        // 사용자 기본정보 
        user: {
            name: '',
            compNo: '',
            deptName: '',
            role: '',
            joinDate: '',
            pto: {
                days: '',
                type: '',
                all: '',
                used: '',
                unused: ''
            }
        },

        // 휴가 구분
        ptoTypes: [],
        selectedPtoType: '',
        selectedSearchPtoType: '',
        applicatePtoType: '',
        endApplication: false,
        endCancelRequest: false,

        // 휴가 신청
        applicateDays: '',
        applicateReason: '',
        applicateDates:[],
        ptoApplication: {
          employeeId: '',
          startDate: '',
          endDate: '',
          reason: '',
          ptoType: ''
          // 추후 결재 정보 추가 될 수 있음.
        },

        searchStartDate: '',
        searchEndDate: '',

        // 휴가 취소
        selectedCancelPtos: [],
        cancelReason: '',
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
        ptoLists: [],

        // 휴가 신청 내역
        defaultPtoHistSelect: {
          name: "휴가 구분",
          value: ""
        },
        searchPtoType:[{name:"전체",value:""}],
        loading: true,
        countPost: 0,
        ptoHistPage: 0,
        ptoHistOptions: {},
        ptoHistLenth: 0,
        footerProps: { 'items-per-page-options': [2, 5] },
        totalPtoHeaders: [
          {
            text: '',
            align: 'start',
            sortable: false,
          },
          { text: '상태', value: 'status' },
          { text: '시작일', value: 'startDate' },
          { text: '종료일', value: 'endDate' },
          { text: '일수', value: 'ptoDays' },
          { text: '휴가구분', value: 'ptoType' },
          { text: '휴가사유', value: 'reason' },
          { text: '취소여부', value: 'cancelYn' },
          { text: '취소일자', value: 'cancelDate' },
          { text: '취소사유', value: 'cancelReason' },
        ],
        ptoHistList: [],
      }),
      watch: {
        ptoHistOptions: {
          handler () {
            this.getPTOHistories()
          },
          deep: true
        },
        searchStartDate: function (newVal, oldVal) {
          if(newVal!==oldVal){
            this.ptoHistOptions.page = 1  
            this.getPTOHistories()
          }
        },
        searchEndDate: function (newVal, oldVal) {
          if(newVal!==oldVal){
            this.ptoHistOptions.page = 1  
            this.getPTOHistories()
          }
        },
        defaultPtoHistSelect: function (newVal, oldVal) {
          if(newVal!==oldVal){
            this.ptoHistOptions.page = 1  
            this.getPTOHistories()
          }
        }
      },
      mounted () {
        this.searchEndDate = this.$moment(new Date()).add(1, 'M').format('YYYY-MM-DD');
        this.searchStartDate = (new Date(new Date().getFullYear(),0,2)).toISOString().substr(0, 10),
        this.loadData()
        if(this.$refs.calendar) this.$refs.calendar.checkChange()
      },
      methods: {
        loadData () {
          this.getUserInfo()
          this.getPTOType ()
          this.getCancellablePTOs()
          this.getPTOHistories()
        },
        showCalendar(){
          let start = {date:this.$moment().startOf('month').format('YYYY-MM-DD')}
          let end = {date:this.$moment().endOf('month').format('YYYY-MM-DD')}
          this.updateRange({start,end})
          this.calendarDialog = true
        },
        checkApplicateDates(dates){
          this.applicateDates = dates.sort();
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
        async updateRange ({ start, end }) {  // 일정 이벤트 갱신
          const events = []

          await this.getCalendarInfo(start,end)
          this.calendarItems.forEach(item => {
            events.push({
              name: item.title,
              start: new Date(item.start),
              end: new Date(item.end),
              color: item.ptoType === 'PTOT000001' ? this.colors[0] : (item.ptoType === 'PTOT000002' ? this.colors[1] : this.colors[2]),
              timed: false,
            })
          })

          this.events = events
        },
        rnd (a, b) {
          return Math.floor((b - a + 1) * Math.random()) + a
        },
        validatePTOApplication(){ // 휴가 신청
          if(!this.selectedPtoType){
            alert('휴가 구분을 선택 해 주세요.')
            return false
          }

          if(this.applicateDates.length != 2){
            alert('시작, 종료일을 정확히 입력 해 주세요.')
            return false
          }
          
          this.applicateDates.sort()

          if( this.applicateDates[1].substring(0,3) >= (new Date().getFullYear()+2) ){
            alert('휴가 신청은 1년 이내 기간 동안 가능합니다.')
            return false
          }

          let startDate = this.applicateDates[0];
          let endDate = this.applicateDates[1];
          let diffDays = (new Date(endDate).getTime() - new Date(startDate).getTime() ) / ( 1000*3600*24 );
          let realUseDays = 0;

          if(this.selectedPtoType.value !== 'PTOT000001' && diffDays > 0){ // 현재 연차 아닐 경우 0.5일로 설정
            alert('반차일 경우 시작일과 종료일은 같아야합니다.')
            return false
          }

          for(let i=0;i<=diffDays;i++){ // 선택한 시작일 ~ 종료일 까지 공휴일, 주말 제외한 실제 사용일 계산
            let currentDay = this.$moment(startDate).add(i,'days');
            realUseDays = this.$holiday.HOLIDAY_MAP.has(this.$moment(currentDay).format('YYYYMMDD')) || (currentDay.isoWeekday() == 6 || currentDay.isoWeekday() == 7) ? realUseDays : realUseDays+1;             
          }
          if(realUseDays === 0){
            alert('실제 휴가 사용일이 없습니다. 날짜를 확인 해주세요')
            return false
          }

          if(this.selectedPtoType.value !== 'PTOT000001'){ // 현재 연차 아닐 경우 0.5일로 설정
            realUseDays = 0.5
          }
          if(realUseDays > this.user.pto.unusedDays){
            alert('잔여 연차 부족')
            return false
          }          
          return true
        },
        async getUserInfo () {  // 기본정보 조회
            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.GET_PTO_INFO.replace('{id}',userId)
            
            await this.axios.get(apiUrl).then((response) => {


              let data = response.data;
              
              if(data.isSuccess){
                
                this.user = {
                    name: data.payload.employee.name,
                    compNo: data.payload.employee.employeeNumber,
                    deptName: data.payload.employee.departmentName,
                    deptCode: data.payload.employee.departmentCode,
                    role: data.payload.employee.position,
                    joinDate: data.payload.employee.hireDate,
                    pto: {
                        days: '',
                        type: '',
                        all: data.payload.occurDays,
                        useDays: data.payload.useDays,
                        unusedDays: data.payload.unusedDays
                    }
                }

              }else{
                alert(data.errorMessage);
              }
            })
        },
        async getPTOType () { // 휴가 구분 조회
           
            let apiUrl = this.$apiUrls.GET_PTO_TYPE
            
            await this.axios.get(apiUrl).then((response) => {

              let data = response.data;
              
              if(data.isSuccess){
                 this.ptoTypes = [];
                 this.searchPtoType = [{name:"전체",value:''}]
                 data.payload.forEach(element => {
                  this.ptoTypes.push({name:element.codeName, value:element.code})   
                  this.searchPtoType.push({name:element.codeName, value:element.code})   
                 });

              }else{
                alert(data.errorMessage);
              }
            })
        },
        async postPTO () {  // 휴가 신청
            this.endApplication = true;
            let pass = this.validatePTOApplication();
            if(!pass){
              this.endApplication = false
              return
            }

            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.POST_PTO.replace('{id}',userId)
            let data = {
                  employeeId: userId,
                  startDate: this.applicateDates[0],
                  endDate: this.applicateDates[1],
                  ptoType: this.selectedPtoType.value,
                  reason: this.applicateReason,
                
            }

            await this.axios.post(apiUrl, data).then((response) => {
              
              let data = response.data;
              
              if(data.isSuccess){
                this.getUserInfo()
                this.getCancellablePTOs()
                this.getPTOHistories()
                alert('휴가 신청이 완료되었습니다.')
              }else{
                alert(data.errorMessage);
              }
              this.endApplication = false
            }).catch(function (error) {
              console.error(error)
              this.endApplication = false
              alert('서비스가 정상적으로 처리되지 않았습니다.')
            })
        },
        async getCancellablePTOs () {  // 취소 가능 휴가 리스트 조회 
            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.GET_CANCELLABLE_PTOS.replace('{id}',userId)
            
            await this.axios.get(apiUrl).then((response) => {
              let data = response.data;
              
              if(data.isSuccess){
                this.selectedCancelPtos = []
                this.ptoLists = []
                let cancellablePTOs = [];
                if(data.payload){
                  data.payload.forEach(e => 
                    cancellablePTOs.push({
                     
                        id: e.ptoHistoryId,
                        start: e.startDate,
                        end: e.endDate,
                        days: e.days,
                        ptoType: e.ptoTypeName
                       
                    }));
                  
                }  
                this.ptoLists = cancellablePTOs;
              }else{
                alert(data.errorMessage);
              }
            }).catch(function (error) {
              console.error(error)
              alert('서비스가 정상적으로 처리되지 않았습니다.')
            })
        },
        async putCancelPTO () {  // 휴가 취소 요청
            this.endCancelRequest = true;

            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.PUT_CANCEL_PTO
            
            if(!this.selectedCancelPtos || this.selectedCancelPtos.length == 0){
              alert('취소하실 휴가를 선택해주세요.')
              this.endCancelRequest = false;
              return
            }
            if(!this.cancelReason){
              alert('취소 사유를 입력해주세요.')
              this.endCancelRequest = false;
              return;
            }
            let cancelPtoHistoryIds = []

            this.selectedCancelPtos.forEach(e => {
              cancelPtoHistoryIds.push(e.id)
            })
            
            let data = {
                  employeeId: userId,
                  cancelReason: this.cancelReason,
                  ptoHistoryIds: cancelPtoHistoryIds,
            }

            await this.axios.put(apiUrl, data).then((response) => {
              
              let data = response.data;
              
              if(data.isSuccess){
                this.cancelReason = ''
                this.getUserInfo()
                this.getCancellablePTOs ()
                this.getPTOHistories()
                alert('휴가 취소가 완료되었습니다.')
              }else{
                alert(data.errorMessage);
              }
              this.endCancelRequest = false
            }).catch(function (error) {
              console.error(error)
              this.endCancelRequest = false
              alert('서비스가 정상적으로 처리되지 않았습니다.')
            }).finally(() => {
              this.selectedCancelPtos = '';
            })
        },
        async getPTOHistories(){
          this.loading = true
          await this.getPTOHistoriesAPI(this.ptoHistOptions)
          this.loading = false
        },
        async getPTOHistoriesAPI () {  // 휴가 신청 내역 조회 
            const { sortBy, sortDesc, page, itemsPerPage} = this.ptoHistOptions
            this.countPost = itemsPerPage
            
            let userId = process.env.VUE_APP_TEST_USER_ID;
            let apiUrl = this.$apiUrls.POST_PTO_HISTORIES.replace('{id}',userId);
            let data = {
              payload: {
                fromDate: this.searchStartDate,
                toDate: this.searchEndDate,
                ptoType: this.defaultPtoHistSelect.value,
                employeeId: userId,
              },
              limit: itemsPerPage,
              page: page,
            }

            await this.axios.post(apiUrl, data).then((response) => {
              
              let data = response.data
              this.countPost = data.payload.totalItems
              this.ptoHistList = data.payload.list
              this.ptoHistLenth = data.payload.totalItems
              
            }).catch(function (error) {
              console.error(error)
              alert('서비스가 정상적으로 처리되지 않았습니다.')
            }).finally(() => {
              this.loading = false;
            })

            if (sortBy.length === 1 && sortDesc.length === 1) {
              this.ptoHistList = this.ptoHistList.sort((a, b) => {
                const sortA = a[sortBy[0]]
                const sortB = b[sortBy[0]]

                if (sortDesc[0]) {
                  if (sortA < sortB) return 1
                  if (sortA > sortB) return -1
                  return 0
                } else {
                  if (sortA < sortB) return -1
                  if (sortA > sortB) return 1
                  return 0
                }
              })
            }

        },
        async getCalendarInfo (start, end) {  // 달력 일정 조회 - 같은 부서 포함
            let apiUrl = this.$apiUrls.GET_CALENDAR_INFO.replace('{deptCode}',this.user.deptCode).replace('{start}',start.date).replace('{end}',end.date)
            
            await this.axios.get(apiUrl).then((response) => {
              let data = response.data;
              
              if(data.isSuccess){
                this.calendarItems = []
                this.calendarItems = data.payload 
              }else{
                alert(data.errorMessage);
              }
            }).catch(function (error) {
              console.error(error)
              alert('서비스가 정상적으로 처리되지 않았습니다.')
            })
        },
      },  // end of methods


};
</script>
