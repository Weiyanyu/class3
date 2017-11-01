<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>

<body>
<h2>Hello World!</h2>



springmvc上传文件
<form name="form1" action="/user/upload_avatar" method="post" enctype="multipart/form-data">
    <input type="file" name="wangEditorH5File" />
    <input type="submit" value="springmvc上传文件" />
</form>


富文本图片上传文件
<form name="form2" action="/user/upload_avatar" method="post" enctype="multipart/form-data">
    <input type="file" name="wangEditorH5File" />
    <input type="submit" value="富文本图片上传文件" />
</form>



<script type="text/javascript" src="//unpkg.com/wangeditor/release/wangEditor.min.js"></script>
<div id="editor">
    <p>欢迎使用 <b>wangEditor</b> 富文本编辑器</p>
</div>

<!-- 注意， 只需要引用 JS，无需引用任何 CSS ！！！-->
<script type="text/javascript">
    var E = window.wangEditor
    var editor = new E('#editor')
    // 或者 var editor = new E( document.getElementById('#editor') )

    editor.customConfig.debug = true

    editor.customConfig.uploadImgServer = '/user/upload_avatar'
    console.log(1);

    editor.customConfig.uploadImgHeaders = {
        'Accept' : 'multipart/form-data'
    };
    editor.customConfig.uploadFileName = 'wangEditorH5File';



    // editor.customConfig.customUploadImg = function (files, insert) {
    // // files 是 input 中选中的文件列表
    // // insert 是获取图片 url 后，插入到编辑器的方法
    //     editor.customConfig.uploadImgParams = {
    //         upload_file : files  // 属性值会自动进行 encode ，此处无需 encode
    //     }
    // // 上传代码返回结果之后，将图片插入到编辑器中
    //     // insert('http://image.class3.com/img/e8acb0b1-ebff-4724-94f7-fe0e8abc3025.png');

    // }

    editor.create()
</script>



</body>
</html>
