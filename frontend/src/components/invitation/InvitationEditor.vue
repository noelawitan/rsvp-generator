<template>
  <form v-if="invitationObj" @submit.prevent="submitInvitation">
    <section id="guestInformation">
      <div class="mb-3 row">
        <label class="col-form-label">Name:</label>
        <div class="col-sm-12 col-md-5 pe-md-0 mt-sm-1">
          <input type="text" class="form-control" id="firstName" v-model="invitationObj.guest.firstName" required
                 @input="validateFirstName" placeholder="First Name">
          <div v-if="firstNameErrorMsg" class="text-danger mt-1">{{ firstNameErrorMsg }}</div>
        </div>
        <div class="col-sm-12 col-md-5 p-md-1 mt-sm-1">
          <input type="text" class="form-control" id="lastName" v-model="invitationObj.guest.lastName" required
                 @input="validateLastName" placeholder="Last Name">
          <div v-if="lastNameErrorMsg" class="text-danger mt-1">{{ lastNameErrorMsg }}</div>
        </div>
        <div class="col-sm-12 col-md-2 ps-md-1 mt-sm-1">
          <input type="text" class="form-control" id="middleName" v-model="invitationObj.guest.middleName" required
                 @input="validateMiddleName" placeholder="M.I.">
          <div v-if="middleNameErrorMsg" class="text-danger mt-1">{{ middleNameErrorMsg }}</div>
        </div>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" id="email" v-model="invitationObj.guest.email" required
               @input="validateEmail">
        <div v-if="emailErrorMsg" class="text-danger">{{ emailErrorMsg }}</div>
      </div>
      <div class="mb-3">
        <label for="gender" class="form-label">Gender</label>
        <select class="form-control" id="gender" v-model="invitationObj.guest.gender" required>
          <option value="" disabled>Select Gender</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
        </select>
      </div>
      <!--TODO: Phone Number -->
    </section>
    <section id="invitationInformation">
      <div class="mb-3">
        <label for="extraAttendees" class="form-label">Extra Attendees</label>
        <input type="number" class="form-control" id="extraAttendees" v-model="invitationObj.extraAttendees" min="0">
      </div>
      <div class="mb-3">
        <label class="form-label">Attending</label>
        <div class="form-check">
          <input class="form-check-input" type="radio" name="attending" id="attendingUndecided" value="null"
                 v-model="invitationObj.attending">
          <label class="form-check-label" for="attendingUndecided">Undecided</label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="radio" name="attending" id="attendingYes" value="true"
                 v-model="invitationObj.attending">
          <label class="form-check-label" for="attendingYes">Yes</label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="radio" name="attending" id="attendingNo" value="false"
                 v-model="invitationObj.attending">
          <label class="form-check-label" for="attendingNo">No</label>
        </div>
      </div>
      <div class="mb-3">
        <div class="form-check">
          <input class="form-check-input" type="checkbox" id="sendInvitation" v-model="invitationObj.sendInvitation">
          <label class="form-check-label" for="sendInvitation">Send Invitation</label>
        </div>
      </div>
      <div class="mb-3" v-if="invitationObj.sendInvitation">
        <label for="responseDeadline" class="form-label">Deadline of Response</label>
        <input type="date" class="form-control" id="responseDeadline" v-model="invitationObj.deadline">
      </div>
    </section>
    <div class="text-center">
      <button type="submit" class="btn btn-primary me-1">Save</button>
      <button type="button" class="btn btn-secondary ms-1" @click="$emit('cancel')">Cancel</button>
    </div>
  </form>
</template>

<script>
export default {
  props: ['invitation'],
  emits: ['cancel', 'submittedInvitation'],
  data() {
    return {
      invitationObj: null,
      firstNameErrorMsg: '',
      lastNameErrorMsg: '',
      middleNameErrorMsg: '',
      emailErrorMsg: ''
    }
  },
  mounted() {
    this.initializeInvitationObj(this.invitation);
  },
  methods: {
    initializeInvitationObj(invitation) {
      this.invitationObj = JSON.parse(JSON.stringify(invitation));
    },
    validateFirstName() {
      if (this.invitationObj.guest.firstName.length < 2) {
        this.firstNameErrorMsg = "First Name must be at least 2 characters long.";
      } else if (this.invitationObj.guest.firstName.length > 25) {
        this.firstNameErrorMsg = "First Name must be no more than 25 characters long.";
      } else {
        this.firstNameErrorMsg = "";
      }
    },
    validateLastName() {
      if (this.invitationObj.guest.lastName.length < 2) {
        this.lastNameErrorMsg = "Last Name must be at least 2 characters long.";
      } else if (this.invitationObj.guest.lastName.length > 25) {
        this.lastNameErrorMsg = "Last Name must be no more than 25 characters long.";
      } else {
        this.lastNameErrorMsg = "";
      }
    },
    validateMiddleName() {
      this.middleNameErrorMsg = '';
      if (this.invitationObj.guest.middleName !== '') {
        if (this.invitationObj.guest.middleName.length > 25) {
          this.lastNameErrorMsg = "Middle Name must be no more than 25 characters long.";
        }
      }
    },
    validateEmail() {
      let emailPattern = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;

      if (!emailPattern.test(this.invitationObj.guest.email)) {
        this.emailErrorMsg = "Invalid email address";
      } else {
        this.emailErrorMsg = "";
      }
    },
    allFieldsValid() {
      return true;
    },
    submitInvitation() {
      this.$emit('submittedInvitation', this.invitationObj);
    }
  }
}
</script>