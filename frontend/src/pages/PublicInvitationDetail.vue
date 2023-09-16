<template>
  <div v-if="invitationDetails"
       class="invitation container d-flex flex-column justify-content-center align-items-center vh-100">
    <div class="row rsvp-container">
      <div class="text-center w-100">
        <h2 class="my-3">{{ invitationDetails.event.name }}</h2>
        <p>Date: {{ formatDate(invitationDetails.event.date) }}</p>
        <p>Time: {{ formatTime(invitationDetails.event.startTime) }} - {{
            formatTime(invitationDetails.event.endTime)
          }}</p>
        <p>Location: {{ invitationDetails.event.location }}</p>
      </div>

      <div class="text-center w-100 my-3">
        <h3>Guest: {{ invitationDetails.guest.firstName }} {{ invitationDetails.guest.middleName }}
          {{ invitationDetails.guest.lastName }}</h3>
      </div>

      <div class="text-center w-100 my-3">
        <label class="mr-3 mb-0">Are you attending?</label>&nbsp;
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="attending" value="true" id="attendingYes">
          <label class="form-check-label" for="attendingYes">Attending</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="attending" value="false" id="attendingNo">
          <label class="form-check-label" for="attendingNo">Not Attending</label>
        </div>
      </div>
      <p v-if="showValidationError" class="text-danger mt-2">Attending selection is required!</p>

      <div v-if="attending === 'true' && attendeeInputs.length > 0" class="right-text-styling w-50 mx-auto">
        <h4>Add Attendees (up to {{ invitationDetails.extraAttendees }})</h4>
        <div v-for="(attendee, index) in attendeeInputs" :key="attendee.id" class="mb-3">

          <label class="d-block">Attendee #{{ index + 1 }}</label>

          <div class="row">
            <div class="col-md-6">
              <input type="text" class="form-control" v-model="attendee.firstName" placeholder="First Name"
                     :class="{'is-invalid': isInvalid(attendee.firstName)}">
              <div v-if="attendee.firstName && !attendee.lastName" class="text-danger mt-2">
                Last Name is required when First Name is provided.
              </div>
              <div v-if="isInvalid(attendee.firstName)" class="text-danger mt-2">
                First Name should be between 2 and 50 characters.
              </div>
            </div>

            <div class="col-md-6 ps-md-0 mt-sm-1 mt-md-0">
              <input type="text" class="form-control" v-model="attendee.lastName" placeholder="Last Name"
                     :class="{'is-invalid': isInvalid(attendee.lastName)}">
              <div v-if="!attendee.firstName && attendee.lastName" class="text-danger mt-2">
                First Name is required when Last Name is provided.
              </div>
              <div v-if="isInvalid(attendee.lastName)" class="text-danger mt-2">
                Last Name should be between 2 and 50 characters.
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-text-styling w-100 mx-auto text-center">
        <button class="btn btn-primary" @click="submit">Submit</button>
        <p>RSVP by: {{ formatDate(invitationDetails.deadLine) }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import {PUBLIC_URL} from "@/js/config";
import {formatDate, formatTime} from "@/js/utility";

export default {
  data() {
    return {
      invitationDetails: null,
      attending: false,
      existingAttendees: [],
      attendeeInputs: [],
      showValidationError: false
    }
  },
  computed: {
    availableAttendeeSlots() {
      return this.invitationDetails.extraAttendees - this.existingAttendees.length;
    }
  },
  mounted() {
    document.body.style.backgroundImage = "url('../rsvp_temp.jpg')";
  },
  created() {
    const publicId = this.$route.params.publicId;

    if (publicId !== '') {
      fetch(`${PUBLIC_URL}/invitation/${publicId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      }).then(response => response.json()).then(data => {
        this.invitationDetails = data;
        this.attending = data.attending ? 'true' : 'false';

        if (data.guest.attendees) {
          this.existingAttendees = data.guest.attendees;

          for (let i = 0; i < this.existingAttendees.length; i++) {
            this.attendeeInputs.push({
              firstName: this.existingAttendees[i].firstName,
              lastName: this.existingAttendees[i].lastName
            });
          }
        } else {
          for (let i = 0; i < data.extraAttendees; i++) {
            this.attendeeInputs.push({firstName: '', lastName: ''});
          }
        }

        for (let i = 0; i < this.availableAttendeeSlots; i++) {
          this.attendeeInputs.push({firstName: '', lastName: ''});
        }
      }).catch((error) => {
        // TODO: Create a modal that says there's something wrong in the server
        console.error('Error:', error);
      });
    }
  },
  methods: {
    formatTime,
    formatDate,
    submit() {
      const publicId = this.$route.params.publicId;
      const invitationUpdate = {
        attending: this.attending,
        attendees: this.attendeeInputs
      };
      this.$loader.isVisible = true;

      fetch(`${PUBLIC_URL}/invitation/${publicId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(invitationUpdate)
      }).then(response => {
        if (response.status === 200) {
          console.log("Success");
        }
      }).catch((error) => {
        // TODO: Create a modal that says there's something wrong in the server
        console.error('Error:', error);
      }).finally(() => {
        this.$loader.isVisible = false;
      });
    },
    isInvalid(str) {
      return !(str.length === 0 || (str.length >= 2 && str.length <= 50));
    }
  }
}
</script>

<style scoped>
.rsvp-container {
  background: rgb(0 0 0 / 60%);
  color: white;
  border-radius: 1%;
}
</style>