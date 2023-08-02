<!DOCTYPE html>
<html>
<head>
    <title>Bill</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
        }

        .shop-icon {
            width: 100px;
            height: 100px;
            margin: 20px auto;
            background-image: url(src/main/resources/images/darzee_logo.png);
            background-size: cover;
            display: inline-block;
            vertical-align: middle;
        }

        .bill-heading {
            text-transform: uppercase;
            font-size: 25px;
        }

        .bill-info {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 20px;
        }

        .bill-info-container {
          display: flex;
          justify-content: space-between;
          width: 80%;
          margin: 0 auto;
        }

        .bill-info-left {
          display: inline-block;
          text-align: left;
        }

        .bill-info-right {
           display: inline-block;
           text-align: right;
         }

        table {
            margin: 20px auto;
            border-collapse: collapse;
            width: 100%;
        }

        table th, table td {
            border: 1px solid #ddd;
            padding: 8px;
        }

        table th {
            background-color: #f2f2f2;
        }

        .right-align {
            text-align: right;
        }

        .gray-background {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="shop-icon"></div>

    <h2 class="bill-heading">${businessName}</h2>

    <div class="bill-info-container">
      <div class="bill-info-left">
        <p>Bill # <span th:text="${orderId}"></span></p>
        <p>Bill To: <span th:text="${customerName}"></span></p>
      </div>
      <div class="bill-info-right">
        <p>Date: <span th:text="${orderCreationDate}"></span></p>
        <p>Order # <span th:text="${orderInvoiceNo}"></span></p>
      </div>
    </div>

    <table>
        <thead>
            <tr>
                <th>S. No</th>
                <th>Item</th>
                <th>Expected Delivery Date</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td><span th:text="${outfitType}"></span></td>
                <td><span th:text="${expectedDeliveryDate}"></span></td>
                <td><span th:text="${totalAmount}"></span></td>
            </tr>
        </tbody>
    </table>

    <div class="right-align">
        <p>Total Amount: <span th:text="${totalAmount}"></span></p>
        <p>Amount Paid: <span th:text="${amountPaid}"></span></p>
        <p>Balance Due: <span th:text="${balanceDue}"></span></p>
    </div>

    <h3 class="gray-background">Thank you for your business</h3>
    <ul style="text-align: left;">
        <li>Once confirmed, any advance amount paid towards the order cannot be refunded.</li>
        <li>Goods once sold will not be taken back.</li>
        <li>We do not give a guarantee of colors. We advise dry cleaning only.</li>
    </ul>

    <p>This is a computer-generated bill</p>

</body>
</html>