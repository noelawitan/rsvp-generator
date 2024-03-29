<template>
  <div v-if="invitationDetails"
       class="container d-flex flex-column justify-content-center align-items-center vh-100">
    <div class="row rsvp-container">
      <div class="text-center w-100 mt-5 mb-2">
        <!-- <h2 class="my-3">{{ invitationDetails.event.name }}</h2>
        <p>Date: {{ formatDate(invitationDetails.event.date) }}</p>
        <p>
          <span>Time: {{ formatTime(invitationDetails.event.startTime) }}</span>
          <span v-if="invitationDetails.event.endTime">&nbsp;-&nbsp;</span>
          <span v-if="invitationDetails.event.endTime">{{ formatTime(invitationDetails.event.endTime) }}</span>
        </p> -->
        <div class="d-flex justify-content-center flex-column align-items-center">
          <div class="row">
            <h5 class="fst-italic" style="font-family: 'Merriweather', serif;">
              You are cordially invited to our wedding.
              Together with our families,
            </h5>
          </div>
          <div class="row">
            <h5 class="fst-italic" style="font-family: 'Merriweather', serif;">
              We hope you can join us to celebrate.
            </h5>
          </div>
        </div>
      </div>

      <!-- <div class="text-center w-100 my-3" v-if="invitationDetails.event.message">
        <p class="fst-italic">
          {{ invitationDetails.event.message }}
        </p>
      </div> -->

      <div class="text-center w-100 my-3">
        <h3>Guest: {{ invitationDetails.guest.firstName }} {{ invitationDetails.guest.middleName }}
          {{ invitationDetails.guest.lastName }}</h3>
      </div>

      <div class="text-center w-100 mt-2">
        <p>Will you be attending?</p>
      </div>
      <div class="text-center w-100">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="attending" :value="true" id="attendingYes">
          <label class="form-check-label" for="attendingYes">Attending</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="attending" :value="false" id="attendingNo">
          <label class="form-check-label" for="attendingNo">Not Attending</label>
        </div>
      </div>
      <p v-if="showValidationError" class="text-danger mt-2">Attending selection is required!</p>
      <br/>
      <div v-if="attending === true && invitationDetails.extraAttendees > 0"
           class="right-text-styling w-75 mx-auto mt-5">
        <div class="row">
          <h4 class="p-0">Is someone else joining with you? (max of {{ invitationDetails.extraAttendees }})</h4>
        </div>
        <div v-for="(attendee, index) in attendeeInputs" :key="attendee.id" class="mb-3">
          <div class="row">
            <label class="p-0 d-block">Attendee #{{ index + 1 }}</label>
          </div>

          <div class="row">
            <div class="col-11 p-0">
              <div class="row">
                <div class="col-xs-12 col-sm-6 mt-1">
                  <input type="text" class="form-control" v-model="attendee.firstName" placeholder="First Name"
                         :class="{'is-invalid': isInvalid(attendee.firstName)}">
                  <div v-if="attendee.firstName && !attendee.lastName" class="text-danger mt-2">
                    Last Name is required when First Name is provided.
                  </div>
                  <div v-if="isInvalid(attendee.firstName)" class="text-danger mt-2">
                    First Name should be between 2 and 50 characters.
                  </div>
                </div>
                <div class="col-xs-12 col-sm-6 ps-sm-0 mt-1">
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
            <div class="col-1 p-0">
              <div class="ms-2 mt-1">
                <button @click="removeAttendee(index)" class="btn btn-danger ml-2">
                  <i class="fa fa-trash"/>
                </button>
              </div>
            </div>
          </div>

        </div>
        <div class="row">
          <button @click="addAttendee" v-if="attendeeInputs.length < invitationDetails.extraAttendees"
                  class="col-2 btn btn-success mb-3">
            <i class="fa fa-plus"/>
          </button>
        </div>
      </div>
      <div class="right-text-styling w-100 mx-auto text-center mb-5">
        <button class="btn btn-primary my-3" @click="submit" :disabled="hasFormErrors">Submit</button>
        <h5 class="fst-italic">
          Kindly RSVP by {{ formatDate(invitationDetails.deadLine) }}
        </h5>
        <h5 class="fst-italic">
          Please note that this is an adults only affair. Thank you for understanding!
        </h5>
      </div>
    </div>
    <public-invitation-success-modal :event-name="invitationDetails.event.name"
                                     :redirect-url="invitationDetails.event.invitationResponseRedirectUrl"
                                     ref="publicInvitationSuccessModal"/>
  </div>
</template>

<script>
import {PUBLIC_URL} from "@/js/config";
import {formatDate, formatTime} from "@/js/utility";
import PublicInvitationSuccessModal from "@/components/PublicInvitationSuccessModal.vue";

export default {
  components: {
    PublicInvitationSuccessModal
  },
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
    hasFormErrors() {
      if(this.attending === null) return true;

      if (this.attending) {
        return this.attendeeInputs.some(attendee => {
          if (attendee.firstName || attendee.lastName) {
            return (
                !this.isValidName(attendee.firstName) ||
                !this.isValidName(attendee.lastName)
            );
          }
          return false;
        });
      }
      return false;
    },
  },
  mounted() {
    document.body.style.backgroundImage = "url('../../rsvp_temp.jpg')";
    document.body.style.backgroundSize = "cover";
    document.body.style.backgroundPosition = "center center";
    document.body.style.backgroundRepeat = "no-repeat";
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
        this.attending = data.attending !== null ? data.attending : null;

        if (data.guest.attendees) {
          this.existingAttendees = data.guest.attendees;

          for (let i = 0; i < this.existingAttendees.length; i++) {
            this.attendeeInputs.push({
              firstName: this.existingAttendees[i].firstName,
              lastName: this.existingAttendees[i].lastName
            });
          }

          if (this.attendeeInputs.length === 0) {
            this.addAttendee();
          }
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
    addAttendee() {
      if (this.attendeeInputs.length < this.invitationDetails.extraAttendees) {
        this.attendeeInputs.push({firstName: '', lastName: ''});
      }
    },
    removeAttendee(index) {
      this.attendeeInputs.splice(index, 1);
    },
    submit() {
      if (!this.hasFormErrors) {
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
            this.$refs.publicInvitationSuccessModal.show();
          }
        }).catch((error) => {
          // TODO: Create a modal that says there's something wrong in the server
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      }
    },
    isInvalid(str) {
      return !(str.length === 0 || (str.length >= 2 && str.length <= 50));
    },
    isValidName(name) {
      return name.length >= 2 && name.length <= 50;
    },
    hasFirstNameOnly(attendee) {
      return attendee.firstName && !attendee.lastName;
    },
    hasLastNameOnly(attendee) {
      return !attendee.firstName && attendee.lastName;
    }
  }
}
</script>

<style scoped>
.rsvp-container {
  background: rgba(0, 0, 0, 0.7); /* Semi-transparent black */
  color: #EFEFEF; /* Light gray text color */
  border-radius: 5px;
  padding: 20px;
}

/* If you want to highlight links or specific text differently, you can add additional styles: */
.rsvp-container a {
  color: #FFD700; /* Example: Gold color for links */
}
</style>