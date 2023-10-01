<template>
  <div>
    <div class="col-md-12 mt-1">
      <div class="card">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <h5 class="card-title mb-0 text-truncate" style="max-width: 250px;" :title="name">
              <i class="fas fa-calendar-alt me-2"></i> {{ name }}
            </h5>
            <small class="text-truncate" :title="formatDate(date)">{{ formatDate(date) }}</small>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <p class="card-text text-truncate" style="max-width: 250px;" :title="location[0].address">
              <i class="fas fa-map-marker-alt me-2"></i> {{ location[0].name }}
            </p>
          </div>
          <div class="d-flex align-items-center">
            <span class="card-text me-1" title="Start Time">
              <i class="fas fa-clock me-2"></i> {{ 'Start: ' + formatTime(startTime) }}
            </span>
            <span v-if="endTime" class="card-text" title="End Time">
              <i class="fas fa-history me-2"></i>{{ 'End: ' + formatTime(endTime) }}
            </span>
          </div>
          <div class="mt-3">
            <button class="btn btn-primary" @click="goToEventDetail">Details</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {formatTime, formatDate} from "@/js/utility";

export default {
  props: {
    id: {
      type: Number,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    date: {
      type: String,
      required: true
    },
    location: {
      type: String,
      required: true
    },
    startTime: {
      type: String,
      required: true
    },
    endTime: {
      type: String,
      required: false,
      default: ''
    }
  },
  methods: {
    formatTime,
    formatDate,
    goToEventDetail() {
      this.$router.push({path: `/event-detail/${this.id}`});
    }
  }
}
</script>