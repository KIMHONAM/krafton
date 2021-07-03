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
            date: this.paramDate ? this.paramDate : (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            dates: this.paramDates ? this.paramDates : [(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
                    (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)],
            menu: false,
            model: this.range ? this.dates : this.date,
            label: this.paramLabel ? this.paramLabel : '값을 입력해주세요.',
        }
    },
    mounted () {
      this.model = this.range ? this.dates : this.date
    },
    methods: {
        allowedDates: val => new Date(val).getDay() !== 0 && new Date(val).getDay() !== 6 && !constants.HOLIDAY_MAP.has(val.replace(/-/gi,'')),
    },
    watch: {
      model(){ 
        this.$emit('updateDate', this.model)
      }
    },
    computed: {
        dateText () {
          return this.range ? (this.model ? this.model.join(' ~ ') : this.dates.join(' ~ ')) : (this.model ? this.model : this.date)
        },
    },
 }
</script>