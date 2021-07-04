<template>
    <v-menu
        ref="menu"
        v-model="menu"
        :close-on-content-click="false"
        :return-value.sync="model"
        transition="scale-transition"
        offset-y
        min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="dateText"
            :label="label"
            prepend-icon="mdi-calendar"
            readonly
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="model"
          :allowed-dates="allowedDates"
          :first-day-of-week="0"
          locale="ko-KR"
          no-title
          scrollable
          :range="range"
        >
          <v-spacer></v-spacer>
          <v-btn
            text
            color="primary"
            @click="menu = false"
          >
            Cancel
          </v-btn>
          <v-btn
            text
            color="primary"
            @click="$refs.menu.save(model)"
          >
            OK
          </v-btn>
        </v-date-picker>
    </v-menu>
</template>

<script>

import constants from '@/constants/holiday'

export default {
    name: 'DatePicker',
    props: {
        paramDate: String,
        range: Boolean,
        paramLabel: String,
        paramDates: Array,
    },
    data () {
        return {
            date: this.paramDate ? this.paramDate : new Date().getDay() == 0 ? this.$moment(Date.now()).add(1,'days').format('YYYY-MM-DD') :
                ((new Date().getDay() == 6 ? this.$moment(Date.now()).add(2,'days').format('YYYY-MM-DD') : this.$moment(Date.now()).format('YYYY-MM-DD'))),
            dates: this.paramDates ? this.paramDates : [ new Date().getDay() == 0 ? this.$moment(Date.now()).add(1,'days').format('YYYY-MM-DD') :
                ((new Date().getDay() == 6 ? this.$moment(Date.now()).add(2,'days').format('YYYY-MM-DD') : this.$moment(Date.now()).format('YYYY-MM-DD'))), new Date().getDay() == 0 ? this.$moment(Date.now()).add(1,'days').format('YYYY-MM-DD') :
                ((new Date().getDay() == 6 ? this.$moment(Date.now()).add(2,'days').format('YYYY-MM-DD') : this.$moment(Date.now()).format('YYYY-MM-DD')))],
            menu: false,
            model: this.range ? this.dates : this.date,
            label: this.paramLabel ? this.paramLabel : '값을 입력해주세요.',
        }
    },
    mounted () {
      this.model = this.range ? this.dates : this.date
    },
    methods: {
        allowedDates: function(val){
          let passed = false;
          if(this.range){
            passed = (new Date(val) >= new Date()) && new Date(val).getDay() !== 0 && new Date(val).getDay() !== 6 && !constants.HOLIDAY_MAP.has(val.replace(/-/gi,''))
          }else{
            passed = new Date(val).getDay() !== 0 && new Date(val).getDay() !== 6 && !constants.HOLIDAY_MAP.has(val.replace(/-/gi,''))
          }          
          return passed
        },
    },
    watch: {
      model(){        
        if(!this.range){
          this.$refs.menu.save(this.model)
        }
        this.$emit('updateDate', this.model)
      },
      menu(){        
        if(this.range){
          if(this.menu == false){
            if(this.model.length<2){            
              this.model.push(this.model[0])
              this.$refs.menu.save(this.model)
            }else{
              this.$refs.menu.save(this.model)
            }
          }
        }
      }
    },
    computed: {
        dateText () {
          if(this.range){
            if(this.model){
              return this.model.length == 2 ? (this.model[1] > this.model[0] ? this.model.join(' ~ ') : this.model[1] + ' ~ ' + this.model[0]) : this.model
            }else{
              return this.dates.length == 2 ? (this.dates[1] > this.dates[0] ? this.dates.join(' ~ ') : this.dates[1] + ' ~ ' + this.dates[0]) : this.dates
            }
          }else{
            if(this.model){
              return this.model
            }else{
              return this.date
            }
          }
        },
    },
 }
</script>