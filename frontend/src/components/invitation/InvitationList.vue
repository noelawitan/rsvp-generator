<template>
  <table class="table table-hover">
    <thead>
    <tr>
      <th>Name</th>
      <th>Attending</th>
      <th>Attendees</th>
      <th>Response Date</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody v-if="invitations">
    <tr v-for="invitation in invitations" :key="invitation.id" :class="isAttendingRowBg(invitation.attending)">
      <td @click="$emit('viewInvitation', invitation.id)" class="hyperlink-style">
        {{ invitation.guest.firstName }} {{ invitation.guest.lastName }}
      </td>
      <td>{{ isAttending(invitation.attending) }}</td>
      <td>{{ invitation.guest.attendees.length }}</td>
      <td>{{ formatDateTime(invitation.responseDateTime) }}</td>
      <td>
        <button class="btn btn-primary me-1" @click="$emit('updateInvitation', invitation.id)">
          <i class="fa fa-pen"/>
        </button>
        <button class="btn btn-danger" @click="$emit('deleteInvitation', invitation.id)">
          <i class="fa fa-trash"/>
        </button>
      </td>
    </tr>
    </tbody>
    <tbody v-else>
    <tr>
      <td class="text-center" colspan="4">
        <p>No Invitation Found</p>
      </td>
    </tr>
    </tbody>
  </table>
  <!--TODO: Make this mobile responsive, by hiding the table and showing an element with collapsable arrow -->
</template>

<script>
import {INVITATION_URL} from "@/js/config";
import {formatDateTime} from "@/js/utility";

export default {
  props: {
    eventId: {
      type: Number,
      required: true
    }
  },
  emits: ['viewInvitation', 'updateInvitation', 'deleteInvitation'],
  data() {
    return {
      invitations: null
    };
  },
  created() {
    this.getAllUserInvitation();
  },
  methods: {
    formatDateTime,
    getAllUserInvitation() {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        fetch(`${INVITATION_URL}/event/${this.eventId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }).then(response => {
              if (response.status === 403) {
                localStorage.clear();
                this.$router.push('/login');
              } else if (response.status === 200) {
                return response.json();
              } else if (response.status === 404) {
                this.invitations = null;
              } else {
                throw new Error(`Unexpected response status: ${response.status}`);
              }
            }).then(data => {
              if (Array.isArray(data)) {
                this.invitations = data;
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
    isAttendingRowBg(attending) {
      let attendingStr = '';

      if (attending === null) {
        // Undecided
        attendingStr = 'table-secondary';
      } else if (attending) {
        // Attending
        attendingStr = 'table-success';
      } else {
        // Not Attending
        attendingStr = 'table-danger';
      }

      return attendingStr;
    }
  }
};
</script>

<style scoped>
.hyperlink-style {
  color: blue;
  text-decoration: underline;
  cursor: pointer;
}

.hyperlink-style:hover {
  color: darkblue;
}
</style>