<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Invoice Layout</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Inter, Arial, sans-serif;
            background-color: #f9f9f9;
            color: #525252;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 40px;
        }

        .header-columns {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eaeaea;
        }

        .two-columns {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .two-columns>div {
            flex: 1;
            margin-right: 20px;
        }

        .two-columns>div:last-child {
            margin-right: 0;
        }

        .left-align {
            text-align: left;
            font-size: 14;
        }

        .right-align {
            text-align: right;
            font-size: 14;
        }

        .order-details {
            margin-top: 40px;
            line-height: 1.5;
        }

        .address {
            margin-top: 50px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table,
        th,
        td {
            border: 1px solid #ddd;
        }

        th,
        td {
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f3f3f3;
        }

        .total {
            text-align: right;
        }

        .total td {
            font-weight: bold;
        }

        .order-summary {
            border: 1px solid #eaeaea;
            padding: 5px 15px 5px 15px;
            background-color: #eef2ff;
            border-radius: 4px;
            margin-top: 40px;
        }

        .order-summary h2 {
            font-size: 16px;
            margin-bottom: 5px;
            border-bottom: 1px solid #eaeaea;
            padding-bottom: 10px;
        }

        .items .item {
            padding-bottom: 10px;
            margin-bottom: 10px;
            line-height: 1.5;
        }

        .items .item:last-child {
            border-bottom: none;
        }

        .item-name {
            font-size: 14px;
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .stitching-cost,
        .material-cost,
        .delivery-date,
        .item-total {
            display: block;
            font-size: 14px;
        }

        .total-line {
            background-color: #eef2ff;
            padding: 5px;
            border-radius: 4px;
        }

        .totals .total-line {
            display: flex;
            justify-content: space-between;
            margin-top: 10px;
        }

        .totals .total-line.total-after-tax {
            font-weight: bold;
        }

        .totals .total-line.balance-due {
            font-size: 16px;
            font-weight: bold;
        }
    </style>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&display=swap" rel="stylesheet" />
</head>

<body>
    <div class="container">
        <div class="header-columns">
            <div class="left-align">
                <h1>Invoice</h1>
                <br />
                <strong>Cult Boutique</strong><br />
                Shop No. 5 Sector K, Ahmedabad<br />
                Gujarat - Pin Code (147001)<br />
                +91 83606 53313<br />
                GST Number: 123456789
            </div>
            <div class="right-align">
                LOGO<br />
                Invoice Date: 123456789<br />
                Invoice Number: 123456789
            </div>
        </div>

        <div class="two-columns">
            <div class="left-align">
                <div class="order-details">
                    <strong>Customer Name:</strong> Balwinder Kumar
                    Singla<br />
                    <strong>Order Number:</strong> 123456789<br />
                    <strong>Order Receive Date:</strong> 31st Oct, 2024<br />
                    <strong>Order Receive Time:</strong> 21:39 IST<br />
                </div>
                <div class="address">
                    <strong>Address:</strong> House Number 4, Sector 40 Near
                    23 No. Phatak, Patiala, Punjab, India - 147001
                </div>
            </div>
            <div class="order-summary-and-totals">
                <div class="order-summary">
                    <h2>Order Summary</h2>
                    <div class="items">
                        <div class="item">
                            <span class="item-name">1. Pant</span>
                            <span class="stitching-cost">stitching cost: 2 x 250 = $500</span>
                            <span class="material-cost">material cost: 2 x 250 = $500</span>
                            <span class="delivery-date">Exp. Delivery Date: 20th Oct, 2023 at 5
                                PM</span>
                            <span class="item-total">Total: $1000</span>
                        </div>
                        <div class="item">
                            <span class="item-name">2. Kurta Pajama</span>
                            <!-- Repeat the same structure as above for each item -->
                        </div>
                        <div class="item">
                            <span class="item-name">3. Nehru Jacket</span>
                            <!-- Repeat the same structure as above for each item -->
                        </div>
                    </div>
                </div>
                <div class="totals">
                    <div class="total-line">
                        <span class="label">Total Before Tax</span>
                        <span class="amount">$3000</span>
                    </div>
                    <div class="total-line">
                        <span class="label">GST (18%)</span>
                        <span class="amount">$540</span>
                    </div>
                    <div class="total-line total-after-tax">
                        <span class="label">Total After Tax</span>
                        <span class="amount">$3540</span>
                    </div>
                    <div class="total-line">
                        <span class="label">Advance (If any)</span>
                        <span class="amount">$3000</span>
                    </div>
                    <div class="total-line balance-due">
                        <span class="label">Balance Due:</span>
                        <span class="amount">$3000</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="left-align">
            Thank You for your business<br />
            <small>Terms and conditions apply.<br />
                *Once confirmed, any advance amount paid towards the order
                cannot be refunded.<br />
                *Goods once sold will not be taken back.<br />
                *We do not give a guarantee of colors. We advise dry
                cleaning only.</small>
        </div>
    </div>
</body>

</html>
