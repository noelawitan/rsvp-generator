<template>
  <the-navbar/>
  <div v-if="event" class="container mt-5">
    <div v-if="event" class="row">
      <div class="card col-sm-12 col-md-12 col-lg-4 h-100">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h3>Event Detail</h3>
            <button type="button" class="btn btn-primary" @click="showUpdateEventModal">
              Edit
            </button>
          </div>
          <hr/>
          <div class="row">
            <div class="col-md-4">
              <label>Name</label>
            </div>
            <div class="col-md-8">
              <p>{{ event.name }}</p>
            </div>
          </div>
          <div class="row" v-for="(location, index) in event.locations" :key="location.id">
            <div class="col-md-4">
              <label>Location #{{ index }}</label>
            </div>
            <div class="col-md-8">
              <p>
                <a class="text-break"
                   target="_blank"
                   :href="location.directionUrl"
                   :title="location.address">
                  {{ location.name }}
                </a>
              </p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <label>Date</label>
            </div>
            <div class="col-md-8">
              <p>{{ formatDate(event.date) }}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <label>Time</label>
            </div>
            <div class="col-md-8">
              <p>{{ formatTime(event.startTime) }} <span v-if="event.endTime"> to {{ formatTime(event.endTime) }}</span>
              </p>
            </div>
          </div>
          <div v-if="event.invitationResponseRedirectUrl" class="row">
            <div class="col-md-4">
              <label>Redirect URL</label>
            </div>
            <div class="col-md-8">
              <p>
                <a :href="event.invitationResponseRedirectUrl" target="_blank">{{
                    event.invitationResponseRedirectUrl
                  }}</a>
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="card col-sm-12 col-md-12 mt-5 col-lg-7 ms-lg-5 mt-lg-0">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h3>All Invitations</h3>
            <button type="button" class="btn btn-primary" @click="showCreateInvitationModal">
              Add Invitation
            </button>
          </div>
          <hr/>
          <invitation-list ref="invitationListRef" :event-id="event.id"
                           @view-invitation="handleViewInvitation"
                           @delete-invitation="handleDeleteInvitation"
                           @update-invitation="handleUpdateInvitation"/>
        </div>
        <view-invitation-modal ref="viewInvitationModalRef"/>
        <create-invitation-modal ref="createInvitationModalRef" :event-obj="event"
                                 @submitted-invitation="handleSubmittedInvitation"/>
        <update-invitation-modal ref="updateInvitationModalRef"
                                 @submitted-invitation="handleSubmittedUpdateInvitation"/>
        <delete-invitation-modal ref="deleteInvitationModalRef" @confirm-delete="handleConfirmedDeleteInvitation"/>
      </div>
      <update-event-modal ref="updateEventModalRef" @submittedEvent="handleSubmittedEvent"
                          :event-obj="event"></update-event-modal>
    </div>
    <div v-else class="row">
      <h4>Event not found</h4>
    </div>

  </div>

</template>

<script>
import TheNavbar from "@/components/TheNavbar.vue";
import {EVENT_URL, INVITATION_URL} from "@/js/config.js";
import {formatTime, formatDate} from "@/js/utility";
import InvitationList from "@/components/invitation/InvitationList.vue";
import UpdateEventModal from "@/components/event/UpdateEventModal.vue";
import ViewInvitationModal from "@/components/invitation/ViewInvitationModal.vue";
import CreateInvitationModal from "@/components/invitation/CreateInvitationModal.vue";
import UpdateInvitationModal from "@/components/invitation/UpdateInvitationModal.vue";
import DeleteInvitationModal from "@/components/invitation/DeleteInvitationModal.vue";

export default {
  components: {
    TheNavbar,
    InvitationList,
    UpdateEventModal,
    CreateInvitationModal,
    ViewInvitationModal,
    UpdateInvitationModal,
    DeleteInvitationModal
  },
  data() {
    return {
      event: null
    }
  },
  created() {
    const eventId = this.$route.params.id;
    const accessToken = localStorage.getItem('access_token');

    if (accessToken !== null) {
      fetch(`${EVENT_URL}/${eventId}`, {
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
        this.event = data;
      }).catch((error) => {
        // TODO: Create a modal that says there's something wrong in the server
        console.error('Error:', error);
      });
    } else {
      this.$router.push('/login');
    }
  },
  methods: {
    formatTime,
    formatDate,
    async handleSubmittedEvent(newEvent) {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        await fetch(`${EVENT_URL}/${newEvent.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          },
          body: JSON.stringify(newEvent),
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
            return;
          }

          return response.json();
        }).then(data => {
          if (data !== null) {
            this.$refs.updateEventModalRef.hide();
            this.event = data;
          }
        }).catch((error) => {
          // TODO: Create a modal that says there's something wrong in the server
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    async handleSubmittedInvitation(newInvitation) {
      //TODO: handle it properly since we're expecting an array here.
      const newInvitations = [];
      newInvitations.push(newInvitation);

      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        await fetch(`${INVITATION_URL}/event/${this.event.id}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          },
          body: JSON.stringify(newInvitations),
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
            return;
          }

          return response.json();
        }).then(data => {
          if (data !== null) {
            this.$refs.createInvitationModalRef.hide();
            this.$refs.invitationListRef.getAllUserInvitation();
          }
        }).catch((error) => {
          // TODO: Create a modal that says there's something wrong in the server
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    async handleSubmittedUpdateInvitation(updatedInvitation) {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        await fetch(`${INVITATION_URL}/${updatedInvitation.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          },
          body: JSON.stringify(updatedInvitation),
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
            return;
          }

          return response.json();
        }).then(data => {
          if (data !== null) {
            this.$refs.updateInvitationModalRef.hide();
            this.$refs.invitationListRef.getAllUserInvitation();
          }
        }).catch((error) => {
          // TODO: Create a modal that says there's something wrong in the server
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    async handleConfirmedDeleteInvitation(invitationId) {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        await fetch(`${INVITATION_URL}/${invitationId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
          } else if (response.status === 200) {
            this.$refs.deleteInvitationModalRef.hide();
            this.$refs.invitationListRef.getAllUserInvitation();
          } else {
            throw new Error(`Unexpected response status: ${response.status}`);
          }
        }).catch(error => {
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    handleViewInvitation(invitationId) {
      this.$refs.viewInvitationModalRef.show(invitationId);
    },
    handleUpdateInvitation(invitationId) {
      this.showUpdateInvitationModal(invitationId);
    },
    handleDeleteInvitation(invitationId) {
      this.$refs.deleteInvitationModalRef.show(invitationId);
    },
    showUpdateEventModal() {
      this.$refs.updateEventModalRef.show();
    },
    showCreateInvitationModal() {
      this.$refs.createInvitationModalRef.show();
    },
    showUpdateInvitationModal(invitationId) {
      this.$refs.updateInvitationModalRef.show(invitationId);
    }
  }
}
</script>