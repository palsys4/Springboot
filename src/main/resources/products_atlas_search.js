[
  {
    $search: {
      compound: {
        should: [
          {
            autocomplete: {
              query: ["Hyalurosmooth"],
              path: "name.en",
              fuzzy: {
                maxEdits: 1,
                prefixLength: 0,
              },
            },
          },
          {
            text: {
              query: ["30535744"],
              path: "key",
            },
          },
          {
            text: {
              query: ["CETIOL"],
              path: "description.en",
            },
          },
          {
            text: {
              query: ["CETIOL"],
              path: "variants.sku",
            },
          },
          {
            autocomplete: {
              query: ["CETIOL"],
              path: "variants.technical_name",
              fuzzy: {
                maxEdits: 1,
                prefixLength: 0,
              },
            },
          },
        ],
      },
    },
  },
  {
    $facet: {
      results: [
        {
          $limit: 10,
        },
        {
          $unwind: {
            path: "$variants",
          },
        },
        {
          $group: {
            _id: "$_id",
            name: {
              $first: "$name.en",
            },
            key: {
              $first: "$key",
            },
            description: {
              $first: "$description.en",
            },
            technicalName: {
              $first: "$variants.technical_name",
            },
            benefits: {
              $first: "$variants.benefits.en",
            },
            productGroups: {
              $first: "$variants.productGroup.en",
            },
            slug: {
              $first: "$slug.en",
            },
          },
        },
        {
          $match: {
            benefits: {
              $in: ["Sulfate Free"],
            },
          },
        },
      ],

      facets: [
        {
          $unwind: "$variants",
        },
        {
          $unwind: "$variants.benefits",
        },
        {
          $unwind: "$variants.productGroup",
        },
        {
          $group: {
            _id: null,
            benefits: {
              $addToSet: "$variants.benefits.en",
            },
            productGroups: {
              $addToSet: "$variants.productGroup.en",
            },
          },
        },
        {
          $project: {
            _id: 0,
            benefits: 1,
            productGroups: 1,
          },
        },
      ],
      total: [
        {
          $count: "count",
        },
      ],
    },
  },
]