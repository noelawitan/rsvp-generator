<template>
  <form v-if="eventObj" @submit.prevent="submitEvent">
    <div class="mb-3">
      <label for="eventName" class="form-label">Name</label>
      <input type="text" class="form-control" id="eventName" v-model="eventObj.name" required
             @input="validateName">
      <div v-if="eventNameError" class="text-danger">{{ eventNameError }}</div>
    </div>
    <div class="mb-3">
      <label for="eventLocation" class="form-label">Location</label>
      <input type="text" class="form-control" id="eventLocation" v-model="eventObj.location" required
             @input="validateLocation">
      <div v-if="eventLocationError" class="text-danger">{{ eventLocationError }}</div>
    </div>
    <div class="mb-3">
      <label for="eventDate" class="form-label">Event Date</label>
      <input type="date" class="form-control" id="eventDate" v-model="eventObj.date" required
             @input="validateDate">
      <div v-if="eventDateError" class="text-danger">{{ eventDateError }}</div>
    </div>
    <div class="mb-3">
      <label for="startTime" class="form-label">Start Time</label>
      <input type="time" class="form-control" id="startTime" v-model="eventObj.startTime" required>
    </div>
    <div class="mb-3">
      <label for="endTime" class="form-label">End Time (Optional)</label>
      <input type="time" class="form-control" id="endTime" v-model="eventObj.endTime"
             @change="validateTime">
      <div v-if="eventEndTimeError" class="text-danger">{{ eventEndTimeError }}</div>
    </div>
    <div class="text-center">
      <button type="submit" class="btn btn-primary me-1">Save</button>
      <button type="button" class="btn btn-secondary ms-1" @click="$emit('cancel')">Cancel</button>
    </div>
  </form>
</template>

<script>
export default {
  props: {
    event: {
      type: Object,
      required: true
    }
  },
  emits: ['cancel', 'submittedEvent'],
  data() {
    return {
      eventObj: null,
      eventNameError: '',
      eventLocationError: '',
      eventDateError: '',
      eventEndTimeError: ''
    }
  },
  mounted() {
    this.initializeEventObj(this.event);
  },
  methods: {
    initializeEventObj(event) {
      this.eventObj = JSON.parse(JSON.stringify(event));
      this.eventNameError = '';
      this.eventLocationError = '';
      this.eventDateError = '';
      this.eventEndTimeError = '';
    },
    submitEvent() {
      if (this.eventNameError === '' && this.eventLocationError === '' && this.eventDateError === '' && this.eventEndTimeError === '') {
        this.$emit('submittedEvent', this.eventObj);
      }
    },
    validateName() {
      if (this.eventObj.name.length < 5) {
        this.eventNameError = "Name must be at least 5 characters long.";
      } else if (this.eventObj.name.length > 50) {
        this.eventNameError = "Name must be no more than 50 characters long.";
      } else {
        this.eventNameError = "";
      }
    },
    validateLocation() {
      if (this.eventObj.location.length < 5) {
        this.eventLocationError = "Location must be at least 5 characters long.";
      } else if (this.eventObj.location.length > 255) {
        this.eventLocationError = "Location must be no more than 255 characters long.";
      } else {
        this.eventLocationError = "";
      }
    },
    validateDate() {
      const today = new Date();
      const selectedDate = new Date(this.eventObj.date);
      // Reset the time part to compare only dates
      today.setHours(0, 0, 0, 0);
      selectedDate.setHours(0, 0, 0, 0);
      if (selectedDate < today) {
        this.eventDateError = "Date must be today or in the future.";
      } else {
        this.eventDateError = "";
      }
    },
    validateTime() {
      if (this.eventObj.endTime !== null) {
        if (this.eventObj.startTime && this.eventObj.endTime) {
          if (this.eventObj.startTime >= this.eventObj.endTime) {
            this.eventEndTimeError = "End time must be greater than start time.";
          } else {
            this.eventEndTimeError = "";
          }
        }
      }
    },
  }
}
</script>