import requests
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

# Configuration
base_url = "https://backend.darzeeapp.com/boutique"
endpoint = "/ledger/reset"
headers = {
    "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3MDkwMzIxNzYxIiwiZXhwIjoyMDAzMTcwMzEyLCJpYXQiOjE2ODc4MTAzMTJ9.HwzkaF9xv4PWuOUY128c4N9Yr8siPsxVvF3iNp9mHkRXw6O409zVUHEuoLsTALA3n17efCbBDBDmdJPK7_YTCg"
}
start_id = 831
end_id = 100000
error_message = "Boutique ledger doesnt exist for boutique"

# Email configuration
smtp_server = 'smtp.gmail.com'
smtp_port = 587
smtp_username = 'tech.darzee@gmail.com'
smtp_password = 'Darzee#2023'
from_email = 'tech.darzee@gmail.com'
to_emails = ['mittalr086@gmail.com', 'singlatushar13@gmail.com']

successful_resets = 0

# Function to send email
def send_email(success_count):
    subject = "Boutique Ledger Reset Summary"
    body = f"Reset ledger is successful for {success_count} boutiques."

    msg = MIMEMultipart()
    msg['From'] = from_email
    msg['To'] = ", ".join(to_emails)
    msg['Subject'] = subject

    msg.attach(MIMEText(body, 'plain'))

    server = smtplib.SMTP(smtp_server, smtp_port)
    server.starttls()
    server.login(smtp_username, smtp_password)
    text = msg.as_string()
    server.sendmail(from_email, to_emails, text)
    server.quit()

# Iterate over boutique IDs and make API calls
for boutique_id in range(start_id, end_id):
    url = f"{base_url}/{boutique_id}{endpoint}"
    response = requests.post(url, headers=headers)

    if response.status_code == 200:
        successful_resets += 1
        print(f"Reset successful for boutique {boutique_id}")
    else:
        print(f"Error resetting boutique {boutique_id}. Status code: {response.status_code}")
        print("Response:", response.text)

        if error_message in response.text:
            print(f"Breaking the loop due to error: {error_message}")
            break

# Send email summary
send_email(successful_resets)
