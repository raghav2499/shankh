<!DOCTYPE html>
<html>
<head>
    <title>Invoice</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
        }

        .invoice-heading {
            font-size: 30px;
            font-weight: bold;
            margin-top: 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 20px;
        }

        .left-section {
            text-align: left;
            width: 50%;
        }

        .right-section {
            text-align: right;
            width: 50%;
        }

        .order-summary {
            background-color: #f2f9fd;
            padding: 10px;
            text-align: right;
            margin-bottom: 20px;
            width: 80%;
            margin: 0 auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        .total {
            font-weight: bold;
        }

        .gray-background {
            background-color: #f2f2f2;
            padding: 10px;
            margin-top: 20px;
            text-align: left;
        }
    </style>
</head>
<body>
    <h1 class="invoice-heading">Invoice</h1>

    <div class="header">
        <div class="left-section">
            <p>Customer Name: <span th:text="${customerName}"></span></p>
            <p>Order Creation Date: <span th:text="${orderCreationDate}"></span></p>
            <p>Order Creation Time: <span th:text="${orderCreationTime}"></span></p>
        </div>
        <div class="right-section">
            <p>Invoice</p>
            <p><span th:text="${businessName}"></span></p>
            <p><span th:text="${tailorContactNo}"></span></p>
            <p>Invoice Number: <span th:text="${invoiceNo}"></span></p>
        </div>
    </div>

    <div class="order-summary">
        <h2>Order Summary</h2>
        <table>
            <thead>
                <tr>
                    <th>Item</th>
                    <th>Price</th>
                    <th>Price Breakup</th>
                    <th>Expected Delivery Date</th>
                </tr>
            </thead>
            <tbody>
                <!-- Dynamically populate rows -->
                <tr th:each="item : ${orderItems}">
                    <td th:text="${item.outfitType}"></td>
                    <td th:text="${item.calculateItemPrice()}"></td>
                    <td>
                        <ul>
                            <li th:each="pb : ${item.getPriceBreakup()}"
                                th:text="${pb.component + ' ' + pb.quantity + ' x ' + pb.value + ' = $' + pb.getTotalBreakUpValue()}">
                            </li>
                        </ul>
                    </td>
                    <td th:text="${item.deliveryDate}"></td>
                </tr>
            </tbody>
        </table>
        <div class="total">
            <p>Total: <span th:text="${totalAmount}"></span></p>
            <p>Advance(if any): <span th:text="${amountPaid}"></span></p>
            <p>Balance Due: <span th:text="${balanceDue}"></span></p>
        </div>
    </div>

    <div class="gray-background">
        <h3>Thank you for your business</h3>
        <ul>
            <li>Once confirmed, any advance amount paid towards the order cannot be refunded.</li>
            <li>Goods once sold will not be taken back.</li>
            <li>We do not give a guarantee of colors. We advise dry cleaning only.</li>
        </ul>
    </div>

    <p>This is a computer-generated invoice</p>

</body>
</html>
