package brainmatics.com.contactdb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayOutputStream;
import java.util.List;

import brainmatics.com.entity.Contact;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.txtFullName)
    @NotEmpty
    EditText txtFullName;

    @BindView(R.id.txtEmail)
    @NotEmpty
    @Email
    EditText txtEmail;

    @BindView(R.id.txtPhone)
    @NotEmpty
    EditText txtPhone;

    @BindView(R.id.txtAddress)
    @NotEmpty
    EditText txtAddress;

    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;

    Validator validator;
    boolean isValid;

    private Bitmap bitmap;

    static final int CAMERA_INTENT = 1999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (shouldAskPermissions()) {
            askPermissions();
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.CAMERA",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    @OnClick(R.id.btnSave)
    public void btnSaveOnClick(){
        if(validate()) {
            Contact contact = new Contact();
            contact.setFullName(txtFullName.getText().toString().trim());
            contact.setEmail(txtEmail.getText().toString().trim());
            contact.setPhone(txtPhone.getText().toString().trim());
            contact.setAddress(txtAddress.getText().toString().trim());
            if(bitmap!=null) {
                contact.setPhoto(encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
            }else{
                contact.setPhoto(null);
            }
            contact.save();
            txtFullName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            Toast.makeText(this, "Contact Tersimpan", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onValidationSucceeded() {
        isValid = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            isValid = false;
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validate() {
        if (validator != null)
            validator.validate();
        return isValid;
    }

    @OnClick(R.id.imgPhoto)
    public void imgPhotoOnClick(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,InputActivity.CAMERA_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == InputActivity.CAMERA_INTENT && resultCode == RESULT_OK) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = getResizedBitmap(bitmap,100,100);
            imgPhoto.setImageBitmap(bitmap);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat,
                                  int quality){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}