<template>
  <form v-if="eventObj" @submit.prevent="submitEvent">
    <div class="row">
      <div class="col-md-6">
        <div class="mb-3">
          <label for="eventName" class="form-label">Name</label>
          <input type="text" class="form-control" id="eventName" v-model="eventObj.name" required
                 @input="validateName">
          <div v-if="eventNameError" class="text-danger">{{ eventNameError }}</div>
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
        <div class="mb-3">
          <label for="eventURL" class="form-label">Response URL Redirect (Optional)</label>
          <input type="url" class="form-control" id="eventURL" v-model="eventObj.invitationResponseRedirectUrl"
                 @change="validateURL">
          <div v-if="eventURLError" class="text-danger">{{ eventURLError }}</div>
        </div>
        <div class="mb-3">
          <label for="eventMessage" class="form-label">Message</label>
          <textarea id="eventMessage" class="form-control" v-model="eventObj.message" @input="validateMessage"
                    :maxlength="500" rows="4"></textarea>
          <div v-if="eventMessageError" class="text-danger">{{ eventMessageError }}</div>
          <div>{{ 500 - eventObj.message.length }} characters left</div>
        </div>
      </div>
      <div class="col-md-6">
        <div v-for="(loc, index) in eventObj.locations" :key="index">
          <h5 class="d-inline-block">{{ loc.locationType }} Location</h5>
          <div v-if="loc.locationType === 'Secondary'" class="d-inline-block ms-2">
            <button @click="removeSecondaryLocation" class="btn btn-danger">
              <i class="fa fa-trash"/>
            </button>
          </div>
          <hr/>
          <div class="mb-3">
            <label :for="'locationName_' + loc.locationType" class="form-label">Name</label>
            <input type="text" :id="'locationName_' + loc.locationType" class="form-control" v-model="loc.name" required
                   @input="validateLocationName(loc, index)">
            <div v-if="locationErrors[index].locationNameError" class="text-danger">
              {{ locationErrors[index].locationNameError }}
            </div>
          </div>
          <div class="mb-3">
            <label :for="'locationAddress_' + loc.locationType" class="form-label">Address</label>
            <input type="text" :id="'locationAddress_' + loc.locationType" class="form-control" v-model="loc.address"
                   required
                   @input="validateLocationAddress(loc, index)">
            <div v-if="locationErrors[index].locationAddressError" class="text-danger">
              {{ locationErrors[index].locationAddressError }}
            </div>
          </div>
          <div class="mb-3">
            <label :for="'location-time-container' + loc.locationType" class="form-label">Time</label>
            <div :id="'location-time-container' + loc.locationType">
              <div class="d-inline-block">
                <input type="time" class="form-control" :id="'locationStartTime_' + loc.locationType"
                       v-model="loc.startTime"
                       @change="validateLocationTime(loc, index)" required>
              </div>
              <span class="d-inline-block">&nbsp;-&nbsp;</span>
              <div class="d-inline-block">
                <input type="time" class="form-control" :id="'locationEndTime_' + loc.locationType"
                       v-model="loc.endTime"
                       @change="validateLocationTime(loc, index)">
              </div>
              <div v-if="locationErrors[index].locationTimeError" class="text-danger">
                {{ locationErrors[index].locationTimeError }}
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label :for="'locationDirectionUrl_' + loc.locationType" class="form-label">Direction Link</label>
            <input type="text" :id="'locationDirectionUrl_' + loc.locationType" class="form-control"
                   v-model="loc.directionUrl"
                   @change="validateLocationURL(loc, index)">
            <div v-if="locationErrors[index].locationDirectionUrlError" class="text-danger">
              {{ locationErrors[index].locationDirectionUrlError }}
            </div>
          </div>
        </div>
        <button v-if="!hasSecondaryLocation" class="btn btn-success" @click.prevent="addSecondaryLocation">
          <li class="fa fa-plus"/>
          Add Secondary Location
        </button>
      </div>
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
      eventDateError: '',
      eventEndTimeError: '',
      eventURLError: '',
      eventMessageError: '',
      locationErrors: []
    }
  },
  computed: {
    hasSecondaryLocation() {
      return this.eventObj.locations.some(loc => loc.locationType === 'Secondary');
    }
  },
  methods: {
    initializeEventObj(event) {
      this.eventObj = JSON.parse(JSON.stringify(event));
      this.eventNameError = '';
      this.eventDateError = '';
      this.eventEndTimeError = '';
      this.eventURLError = '';
      this.locationErrors = new Array(event.locations.length).fill({
        locationNameError: '',
        locationAddressError: '',
        locationTimeError: '',
        locationDirectionUrlError: ''
      });
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
    validateURL() {
      try {
        new URL(this.eventObj.invitationResponseRedirectUrl);
        this.eventURLError = '';
      } catch (_) {
        this.eventURLError = 'Invalid URL format';
      }
    },
    validateMessage() {
      if (this.eventObj.message.length > 500) {
        this.eventMessageError = "Message must be no more than 500 characters long.";
      } else {
        this.eventMessageError = "";
      }
    },
    validateLocationName(loc, index) {
      if (!loc.name) return true;

      const isValid = loc.name.length > 5 && loc.name.length < 25;
      this.locationErrors[index].locationNameError = isValid ? '' : 'Location name must be between 5 and 25 characters long.';
      return isValid;
    },
    validateLocationAddress(loc, index) {
      if (!loc.address) return true;

      const isValid = loc.address.length > 10 && loc.address.length < 255;
      this.locationErrors[index].locationAddressError = isValid ? '' : 'Address must be between 25 and 255 characters long.'
      return isValid;
    },
    validateLocationURL(loc, index) {
      const urlPattern = new RegExp('^(https?:\\/\\/)?' +
          '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.?)+[a-z]{2,}|' +
          '((\\d{1,3}\\.){3}\\d{1,3}))' +
          '(\\:\\d+)?' +
          '(\\/[-a-z\\d%@_.~+&:]*)*' +
          '(\\?[;&a-z\\d%@_=.~+&:]*)?' +
          '(\\#[-a-z\\d_]*)?$', 'i');

      if (!urlPattern.test(loc.directionUrl)) {
        this.locationErrors[index].locationDirectionUrlError = 'Invalid URL';
      } else {
        this.locationErrors[index].locationDirectionUrlError = '';
      }
    },
    validateLocationTime(loc, index) {
      let error = '';
      let startTime = `${loc.startTime}:00`;
      let endTime = `${loc.endTime}:00`;

      if (loc.startTime && loc.endTime) {
        if (startTime >= endTime) {
          error = "End time must be greater than start time.";
        } else if (startTime < this.eventObj.startTime) {
          error = "Start time must not be less than event start time.";
        } else if (this.eventObj.endTime !== null && endTime > this.eventObj.endTime) {
          error = "End time must not be greater than event end time.";
        } else if (endTime < this.eventObj.startTime) {
          error = "End time must be greater than event start time.";
        }
      }

      this.locationErrors[index].locationTimeError = error;
      return error;
    },
    addSecondaryLocation() {
      this.eventObj.locations.push({
        locationType: 'Secondary',
        name: '',
        address: '',
        directionUrl: ''
      });

      this.locationErrors.push({
        locationNameError: '',
        locationAddressError: '',
        locationTimeError: '',
        locationDirectionUrlError: ''
      });

    },
    removeSecondaryLocation() {
      const index = this.eventObj.locations.findIndex(loc => loc.locationType === 'Secondary');
      if (index > -1) {
        this.eventObj.locations.splice(index, 1);
        this.locationErrors.splice(index, 1);
      }
    },
    allFieldsValid() {
      let isAllValid = [this.eventNameError, this.eventDateError, this.eventEndTimeError, this.eventURLError, this.eventMessageError]
          .every(error => error === '');

      for (let i = 0; this.locationErrors.length > i; i++) {
        let locErr = this.locationErrors[i];
        isAllValid = [locErr.locationNameError, locErr.locationAddressError, locErr.locationTimeError, locErr.locationDirectionUrlError]
            .every(error => error === '');
      }

      return isAllValid;
    },
    submitEvent() {
      if (this.allFieldsValid()) {
        this.$emit('submittedEvent', this.eventObj);
      }
    },
  }
}
</script>