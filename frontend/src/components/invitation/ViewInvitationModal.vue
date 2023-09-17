<template>
  <div class="modal fade" id="viewInvitationModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Invitation Details</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div class="row" v-if="invitationObj">
            <div class="col-md-12 col-lg-6">
              <h5>Guest Information</h5>
              <hr/>
              <div class="row">
                <div class="col-md-3">
                  <label>Name</label>
                </div>
                <div class="col-md-9">
                  <p>
                    <span>{{ invitationObj.guest.firstName }}&nbsp;</span>
                    <span>{{ invitationObj.guest.lastName }}&nbsp;</span>
                  </p>
                </div>
              </div>
              <div class="row" v-if="invitationObj.guest.email">
                <div class="col-md-3">
                  <label>Email</label>
                </div>
                <div class="col-md-9">
                  <p>{{ invitationObj.guest.email }}</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <label>Attending</label>
                </div>
                <div class="col-md-9">
                  <p>{{ isAttending(invitationObj.attending) }}</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <label>Link</label>
                </div>
                <div class="col-md-9">
                  <p>
                    <a :href="invitationObj.invitationUrl" :title="invitationObj.invitationUrl" target="_blank">
                      {{ invitationObj.invitationUrl }}
                    </a>
                    <button @click="copyToClipboard(invitationObj.invitationUrl)" class="btn btn-link">
                      <i class="fas fa-copy"></i>
                    </button>
                  </p>
                </div>
              </div>
              <div class="row" v-if="invitationObj.sentDate">
                <div class="col-md-3">
                  <label>Sent Date</label>
                </div>
                <div class="col-md-9">
                  <p>{{ formatDate(invitationObj.sentDate) }}</p>
                </div>
              </div>
              <div class="row" v-if="invitationObj.deadLine">
                <div class="col-md-3">
                  <label>Deadline</label>
                </div>
                <div class="col-md-9">
                  <p>{{ formatDate(invitationObj.deadLine) }}</p>
                </div>
              </div>
              <div class="row" v-if="invitationObj.responseDateTime">
                <div class="col-md-3">
                  <label>Response</label>
                </div>
                <div class="col-md-9">
                  <p>{{ formatDateTime(invitationObj.responseDateTime) }}</p>
                </div>
              </div>
            </div>
            <div class="col-md-12 col-lg-6">
              <h5>Additional Attendees</h5>
              <hr/>
              <div v-if="invitationObj.guest.attendees !== null && invitationObj.guest.attendees.length > 0">
                <div class="row" v-for="(attendee, index) in invitationObj.guest.attendees" :key="attendee.id">
                  <div class="col-md-4">
                    <label>Attendee #{{ index + 1 }}</label>
                  </div>
                  <div class="col-md-8">
                    <p>{{ attendee.firstName }} {{ attendee.lastName }}</p>
                  </div>
                </div>
              </div>
              <div v-else>
                <p>No additional attendees.</p>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <div class="text-center w-100">
            <button type="button" class="btn btn-secondary" @click="hide">Close</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import Modal from "bootstrap/js/dist/modal";
import {INVITATION_URL} from "@/js/config";
import {formatDate, formatDateTime} from "@/js/utility";

export default {
  data() {
    return {
      viewInvitationModal: null,
      invitationObj: null
    }
  },
  mounted() {
    this.viewInvitationModal = new Modal('#viewInvitationModal');
  },
  methods: {
    formatDateTime,
    formatDate,
    isAttending(attending) {
      let attendingStr = 'Undecided';

      if (attending !== null) {
        if (attending) {
          attendingStr = 'Yes';
        } else {
          attendingStr = 'No';
        }
      }

      return attendingStr;
    },
    copyToClipboard(link) {
      navigator.clipboard.writeText(link);
      alert("Link copied to clipboard!");
    },
    show(invitationId) {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        fetch(`${INVITATION_URL}/${invitationId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
            return;
          }

          return response.json();
        }).then(data => {
          this.invitationObj = data;
          this.viewInvitationModal.show();
        }).catch((error) => {
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    hide() {
      this.viewInvitationModal.hide();
    }
  }
}
</script>