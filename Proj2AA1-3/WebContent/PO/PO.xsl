<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
		      <title>PO number <xsl:value-of select="/order/@id"/> for <xsl:value-of select="/order/customer/@account"/></title>
		    </head>
		    <body>
				<table>
					<tr>
						<td>PO ID</td>
						<td><xsl:value-of select="/order/@id"/></td>
					</tr>
					<tr>
						<td>Customer Account</td>
						<td><xsl:value-of select="/order/customer/@account"/></td>
					</tr>
					<tr>
						<td>Customer Name</td>
						<td><xsl:value-of select="/order/customer/name"/></td>
					</tr>
					<tr>
						<td>Address</td>
						<td><xsl:value-of select="/order/customer/address/street"/></td>
						<td><xsl:value-of select="/order/customer/address/city"/></td>
						<td><xsl:value-of select="/order/customer/address/province"/></td>
						<td><xsl:value-of select="/order/customer/address/postal"/></td>
					</tr>
				</table>
				<br /><br />
				Items:
				<table border = "1">
					<tr>
						<th>ID</th><th>Name</th><th>Price</th><th>Quantity</th><th>Extended</th>
					</tr>
					<xsl:for-each select="/order/items/item">
						<tr>
							<td><xsl:value-of select="@number"/></td>
							<td><xsl:value-of select="name"/></td>
							<td><xsl:value-of select="price"/></td>
							<td><xsl:value-of select="quantity"/></td>
							<td><xsl:value-of select="extended"/></td>
						</tr>
					</xsl:for-each>
				</table>
				<br /><br />
				<table>
					<tr>
						<td>Total</td>
						<td><xsl:value-of select="/order/total"/></td>
					</tr>
					<tr>
						<td>PST</td>
						<td><xsl:value-of select="/order/PST"/></td>
					</tr>
					<tr>
						<td>Shipping</td>
						<td><xsl:value-of select="/order/shipping"/></td>
					</tr>
					<tr>
						<td>GST</td>
						<td><xsl:value-of select="/order/GST"/></td>
					</tr>
					<tr>
						<td>Grand Total</td>
						<td><xsl:value-of select="/order/total"/></td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>