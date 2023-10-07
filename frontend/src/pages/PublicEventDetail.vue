<template>
  <div class="container d-flex flex-column justify-content-center align-items-center vh-100">
    <div class="row rsvp-container mb-2">
      <div class="text-center w-100 mt-2 mb-2">
        <div class="d-flex justify-content-center flex-column align-items-center">
          <h5>
            Please enter your name below to confirm your invitation for our wedding.
          </h5>
        </div>
      </div>
      <hr/>
      <div class="input-group">
        <span class="bg-purple input-group-text" style="border:none;">
          <i class="fas fa-search"></i>
        </span>
        <div class="form-floating">
          <input type="text" class="form-control" id="searchGuestInput" placeholder="Search Guest" v-model="inputText"
                 @input="onInput">
          <label for="searchGuestInput" style="color:#cccccc">Search Guest</label>
        </div>
        <span class="bg-purple input-group-text" style="border:none;min-width:40px;">
          <span class="spinner-border spinner-border-sm" role="status" v-if="loading">
            <span class="visually-hidden">Loading...</span>
          </span>
        </span>
      </div>
    </div>
    <div class="autocomplete w-75">
      <div class="autocomplete-results text-center py-1 fs-5 text fw-bold" v-if="results.length">
        <a
            v-for="item in results"
            :key="item.guestName"
            :href="item.invitationUrl"
            class="autocomplete-result text-decoration-none"
        >
          <p class="mb-0"><i class="fas fa-user"/> {{ item.guestName }}</p>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import {PUBLIC_URL} from "@/js/config";

export default {
  data() {
    return {
      publicId: null,
      inputText: null,
      results: [],
      loading: false,
      listId: 'search-results'
    };
  },
  mounted() {
    document.body.style.backgroundImage = "url('../../rsvp_temp.jpg')";
    document.body.style.backgroundSize = "cover";
    document.body.style.backgroundPosition = "center center";
    document.body.style.backgroundRepeat = "no-repeat";
  },
  created() {
    this.publicId = this.$route.params.publicId;
  },
  methods: {
    onInput() {
      if (this.inputText.length < 3) {
        this.results = [];
        return;
      }
      this.loading = true;
      fetch(`${PUBLIC_URL}/event/${this.publicId}/search?keyword=${this.inputText}`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            this.results = data;
          })
          .catch(error => {
            console.error('Error fetching results:', error);
          })
          .finally(() => {
            this.loading = false;
          });
    },
    redirect(url) {
      window.location.href = url;
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

.autocomplete-results {
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>