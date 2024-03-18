<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Order Invoice</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter&display=swap" rel="stylesheet">
    <style>
        * {
            font-family: "Inter", sans-serif;
            font-optical-sizing: auto;
            font-weight: 400;
            font-style: normal;
            font-variation-settings: "slnt" 0;
        }
        .main-div-header,
        .main-div-content {
            display: flex;
        }
        .left {
            flex: 1;
            padding: 10px;
        }
        .right {
            flex:1;
            margin-right: 20px;
            margin-top: 5rem;
        }
        .main-div-content-right {
            flex: 1;
            margin-top: 1rem;
            margin-left: 20px;
        }
        .order-summary {
            background-color: #eef2ff;
            padding: 20px;
        }
        .gray-background {
            display: flex;
            justify-content: center; /* Center horizontally */
            align-items: center; /* Center vertically */
            background-color: #fff;
            padding: 10px;
            margin-top: 40px;
        }
        .total,
        .total1 {
            background-color: #eef2ff;
            margin-top: 10px;
            padding: 5px;
        }
        #balanceDue {
            color: red;
        }
        .invoice {
            color: #525252;
        }
        .black {
            color: black;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="main-div-header">
        <div class="left mt-5">
            <h1 class="invoice">Invoice</h1>
            <p class="fw-bold">Botique Name</p>
        </div>
        <div class="right">
            <p class="invoice">Order Number : <span class="fw-bold black">07-03-2024</span></p>
            <p class="invoice">Invoice Number : <span class="fw-bold black">123-468-123</span></p>
        </div>
        <hr>
    </div>
    <hr>
    <div class="main-div-content">
        <div class="left mt-2">
            <div class="customer-details">
                <p class="invoice">Customer Name: <span class="fw-bold black">${customerName}</span></p>
                <p class="invoice">Order Number: <span class="fw-bold black">${orderNumber}</span></p>
                <p class="invoice">Order Received Date: <span class="fw-bold black">${orderReceivedDate}</span></p>
                <p class="invoice">Order Received Time: <span class="fw-bold black">${orderReceivedTime}</span></p>
            </div>
        </div>
        <div class="main-div-content-right">
            <div class="order-summary">
                <h5 class="invoice">Order Summary</h5>
                <hr>
                <table class="table table-bordered">
                    <thead class="thead-dark">
                    <tr>
                        <th>Item</th>
                        <th>Price</th>
                        <th>Price Breakup</th>
                        <th>Expected Delivery Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderItems as item>
                    <tr>
                        <td>${item.outfitType}</td>
                        <td>${item.calculateItemPrice()}</td>
                        <td>
                            <ul>
                                <#list item.getPriceBreakup() as pb>
                                <li>${pb.component} ${pb.quantity} x ${pb.value} = $${pb.getTotalBreakUpValue()}</li>
                            </#list>
                            </ul>
                        </td>
                        <td>${item.deliveryDate}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="total">
                <div class="container">
                    <p class="mt-2 fw-bold">Advance(if any): <span id="amountPaid" class="float-end">${advanceAmount}</span></p>
                </div>
            </div>
            <div class="total1">
                <div class="container">
                    <p class="mt-2 fw-bold">Balance Due: <span id="balanceDue" class="float-end">${balanceDueAmount}</span></p>
                </div>
            </div>
        </div>
        <hr>
    </div>
    <hr>
    <div class="gray-background">
        <div class="container">
            <h3 class="fw-bolder invoice">Thank you for your business</h3>
            <p>Terms and conditions</p>
            <ul>
                <li class="invoice">Once confirmed, any advance amount paid towards the order cannot be refunded.</li>
                <li class="invoice">Goods once sold will not be taken back.</li>
                <li class="invoice">We do not give a guarantee of colors. We advise dry cleaning only.</li>
            </ul>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
