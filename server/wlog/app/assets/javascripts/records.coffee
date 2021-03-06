jQuery ($) ->

   # DOM element and DOM meta-data.
   $table = $('.container table')
   productListUrl = $table.data('list')

   # Load one column of id
   loadProductTable = ->
      $.get productListUrl, (records) ->
         $.each records, (index, id) ->
            row  = $('<tr/>').append $('<td/>').text(id)
            row.attr 'contenteditable', true
            $table.append row
            loadProductDetails row

   # Construct a URL by replacing the id parameter
   productDetailsUrl = (id) ->
      $table.data('details').replace '0', id

   # Load and append product details to a table row.
   loadProductDetails = (tableRow) ->
      id = tableRow.text()
      $.get productDetailsUrl(id), (record) ->
         tableRow.append $('<td/>').text(record.value)
         tableRow.append $('<td/>').text(record.userid)
         tableRow.append $('<td/>').text(record.ts)
         tableRow.append $('<td/>').text(record.weight)
         tableRow.append $('<td/>')

   loadProductTable()


   # Save an edited table row to the server
   saveRow = ($row) ->
      [id, value, userid, ts, weight] = $row.children().map -> $(this).text()
      record =
         id: parseInt(id)
         value: value
         userid: userid
         ts: ts
         weight: weight
      jqxhr = $.ajax
         type: "PUT"
         url: productDetailsUrl(id)
         contentType: "application/json"
         data: JSON.stringify record
      jqxhr.done (response) ->
         $label = $('<span/>').addClass('label label-success')
         $row.children().last().append $label.text(response)
         $label.delay(3000).fadeOut()
      jqxhr.fail (data) ->
         $label = $('<span/>').addClass('label label-important')
         message = data.responseText || data.statusText
         $row.children().last().append $label.text(message)

   # Attach edit handling to editable elements.
   $('[contenteditable]').live 'blur', ->
         saveRow $(this)
