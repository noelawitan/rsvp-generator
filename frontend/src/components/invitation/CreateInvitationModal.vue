<template>
  <div class="modal fade" id="createInvitationModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Create Invitation</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <invitation-editor ref="createInvitationEditor" :invitation="invitation"
                             @submitted-invitation="handleSubmittedInvitation"
                             @cancel="hide"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import InvitationEditor from "@/components/invitation/InvitationEditor.vue";
import Modal from "bootstrap/js/dist/modal";

export default {
  components: {InvitationEditor},
  emits: ['submittedInvitation'],
  props: {
    eventObj: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      createInvitationModal: null,
      invitation: {
        guest: {
          firstName: '',
          lastName: '',
          middleName: '',
          gender: '',
          email: '',
          phoneNumber: ''
        },
        event: this.eventObj,
        attending: null,
        deadLine: '',
        sendInvitation: true,
        extraAttendees: 0
      }
    };
  },
  mounted() {
    this.createInvitationModal = new Modal('#createInvitationModal');
  },
  methods: {
    clearData() {
      this.invitation = {
        guest: {
          firstName: '',
          lastName: '',
          middleName: '',
          gender: '',
          email: '',
          phoneNumber: ''
        },
        event: this.eventObj,
        attending: null,
        deadLine: '',
        sendInvitation: true
      };
    },
    handleSubmittedInvitation(newInvitation) {
      this.$emit('submittedInvitation', newInvitation);
    },
    show() {
      this.clearData();
      this.$refs.createInvitationEditor.initializeInvitationObj(this.invitation);
      this.createInvitationModal.show();
    },
    hide() {
      this.createInvitationModal.hide();
    }
  }
}
</script>

