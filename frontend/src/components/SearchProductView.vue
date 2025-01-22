<template>

    <v-data-table
        :headers="headers"
        :items="searchProduct"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'SearchProductView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            searchProduct : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/searchProducts'))

            temp.data._embedded.searchProducts.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.searchProduct = temp.data._embedded.searchProducts;
        },
        methods: {
        }
    }
</script>

