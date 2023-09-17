<template>
  <div class="modal fade" id="successResponseModal" tabindex="-1" ref="myModal" data-backdrop="static"
       data-keyboard="false">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header text-center">
          <h5 class="modal-title">Thank you for your response</h5>
        </div>
        <div class="modal-body">
          <div v-if="redirectUrlValue">
            You will be automatically redirected to <strong>{{ eventNameValue }} </strong> website in
            <strong>{{ seconds }} seconds</strong>, or you can <a class="link-success" @click="redirectImmediately">click here</a>
            to proceed immediately.
          </div>
          <div v-else>
            <p>The response is successfully posted. We appreciate you taking the time to let us know your decision.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";

export default {
  props: {
    eventName: {
      type: String,
      required: true
    },
    redirectUrl: {
      type: String,
      required: false
    }
  },
  data() {
    return {
      successResponseModal: null,
      eventNameValue: this.eventName,
      redirectUrlValue: this.redirectUrl,
      interval: null,
      seconds: 5
    }
  },
  mounted() {
    this.successResponseModal = new Modal("#successResponseModal", {
      backdrop: 'static',
      keyboard: false
    });
  },
  methods: {
    show() {
      this.successResponseModal.show();

      if (this.redirectUrlValue) {
        let interval = setInterval(() => {
          this.seconds--;
          if (this.seconds <= 0) {
            clearInterval(interval);
            this.redirect();
          }
        }, 1000);
      }
    },
    redirect() {
      if (this.redirectUrlValue !== null) {
        window.location.href = this.redirectUrlValue;
      }
    },
    redirectImmediately() {
      // Set this to 1 to prevent it from becoming negative, as the setInterval is still running.
      this.seconds = 1;
      this.redirect();
    }
  }
}
</script>