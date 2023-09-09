<template>
  <div class="modal fade" id="updateInvitationModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Invitation</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <invitation-editor ref="updateInvitationEditor" :invitation="invitationObj" @submitted-invitation="handleSubmittedInvitation"
                             @cancel="hide"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";
import InvitationEditor from "@/components/invitation/InvitationEditor.vue";
import {INVITATION_URL} from "@/config/config";

export default {
  components: {InvitationEditor},
  emits: ['submittedInvitation'],
  data() {
    return {
      updateInvitationModal: null,
      invitationObj: null
    };
  },
  mounted() {
    this.updateInvitationModal = new Modal('#updateInvitationModal');
  },
  methods: {
    handleSubmittedInvitation(updatedInvitation) {
      this.$emit('submittedInvitation', updatedInvitation);
    },
    show(invitationId) {
      this.invitation = JSON.parse(JSON.stringify(this.invitationObj));

      const accessToken = localStorage.getItem('access_token');

      if (accessToken !== null) {
        fetch(`${INVITATION_URL}/${invitationId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        })
            .then(response => {
              if (response.status === 403) {
                this.$router.push('/login');
                return;
              }

              return response.json();
            })
            .then(data => {
              if(data !== null) {
                if ('deadLine' in data) {
                  if (data.deadLine !== null) {
                    data['sendInvitation'] = true;
                  }
                }
                this.$refs.updateInvitationEditor.initializeInvitationObj(data);
                this.updateInvitationModal.show();
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            });
      } else {
        this.$router.push('/login');
      }
    },
    hide() {
      this.updateInvitationModal.hide();
    }
  }
};
</script>