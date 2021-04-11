[![CircleCI](https://circleci.com/gh/vinnersafterwork/truman-ms-android.svg?style=svg&circle-token=fbec58099f9ba8af3ad6df4f74081701123114df)](https://app.circleci.com/pipelines/github/vinnersafterwork/truman-ms-android)


# Truman ms
**This Code is property of Vinners Afterworksol Pvt Ltd.**

---

## Branches And Their Purpose

1. `master` - Main Development Branch that will be contain the latest code
2. `beta-closed` - branch that will contain latest closed beta release
            also releasing code to this branch will trigger a CI-CD action
            that will release a new Update To Closed testing group `vinners_internal_team`, more info in deployment section
2. `beta-open` - branch that will contain latest open beta release
            also releasing code to this branch will trigger a CI-CD action
            that will release a new Update To Open testing group `public_tester_group`, more info in deployment section
3. `production` - This branch contains the lastest play store build code, no cd action associated with this branch now
---

## deployment
to deploy an app update to 

1. `vinners_internal_team` - push a commit to `beta-closed` branch
2. `public_tester_group` - push a commit to `beta-open` branch
3. `Play store` - build manually and upload app bundle to play store
---

## Testing & Testers

You Can Enroll Yourself In Beta Testing using this link

1. For Vinners Internal Team (`vinners_internal_team`)
https://appdistribution.firebase.dev/i/vfaqD4wt

2. For Public Testing Team (`public_tester_group`)
https://appdistribution.firebase.dev/i/Asnx5pgX

---
