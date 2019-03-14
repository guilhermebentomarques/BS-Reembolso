package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Guilherme on 17/03/2016.
 */
public class CLG_Fotos {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void prcVerificaPermissaoDiretorio(Activity activity) {
        //VERIFICA SE TEM PERMISSÃO DE ESCRITA.
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //SE NÃO TEM PERMISSÃO DE ESCRITA, CRIA DIALOG MOSTRANDO A MENSAGEM PARA DAR PERMISSÃO.
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public File prcCriaArquivoImagem(String _psNomePasta, String _psNomeFoto) throws IOException {
        File direct = new File(Environment.getExternalStorageDirectory() + "/" + _psNomePasta + "");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/" + _psNomePasta + "/");
            wallpaperDirectory.mkdirs();
        }

        //File _fImagem = File.createTempFile("" + _psNomeFoto + "_", ".jpg", direct);
        File _fImagem = new File(direct, _psNomeFoto + ".jpg");

        return _fImagem;
    }

    public void prcExibeFotoNaGaleria(Context context, File _psFile) {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            // Save a file: path for use with ACTION_VIEW intents
            String _psImagem = "file:" + _psFile.getAbsolutePath();
            prcScanearArquivo(_psFile.getAbsolutePath(), context);
            Uri contentUri = Uri.fromFile(_psFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
        catch (Exception ex)
        {

        }
    }

    private void prcScanearArquivo(String path, Context context) {
        //ATUALIZA ARQUIVOS NA GALERIA
        MediaScannerConnection.scanFile(context, new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        //Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    public static String prcAlteraFotoParaBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } catch (Exception ex) {
        }
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public void prcAdicionaFoto_ImageView(ImageView img, String PhotoPath) {
        // Get the dimensions of the View
        int targetW = img.getWidth();
        int targetH = img.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(PhotoPath, bmOptions);
        //BitmapFactory.decodeFile("File:" + PhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath, bmOptions);
        img.setImageBitmap(bitmap);
    }

    public void prcRedimensionaFoto(File _psFotoFile, Integer _iMinWidth, Integer _iQualidade0a100) throws IOException {
        try {        //INICIAMOS COM A IMAGEM ORIGINAL ABERTA
            File imgFileOrig = _psFotoFile; //BUSCAMOS O ARQUIVO E ABRIMOS
            Bitmap b = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath());

            //TAMANHO ORIGINAL
            int origWidth = b.getWidth();
            int origHeight = b.getHeight();

            final int destWidth = _iMinWidth; //TAMANHO MAXIMO

            if (origWidth > destWidth) {
                //SE É MAIORQ QUE O TAMANHO DESEJADO, CALCULAMOS O VALOR
                int destHeight = origHeight / (origWidth / destWidth);
                //ESCALAMOS A IMAGE E NÃO SÓ REDIMENSIONAMOS
                Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                //COMPRESSA A IMAGEM PARA A QUALIDADE DESEJADA
                b2.compress(Bitmap.CompressFormat.JPEG, _iQualidade0a100, outStream);
                //SALVA O ARQUIVO E FECHAO MESMO

                _psFotoFile.createNewFile();
                //ESCREVE OS BITES NO ARQUIVO
                FileOutputStream fo = new FileOutputStream(_psFotoFile);
                fo.write(outStream.toByteArray());
                fo.close();
            }
        }
        catch (Exception ex){

        }
    }

    public void prcRedimensionaFoto_Porcentagem(File _psFotoFile, Integer _iProporcao, Integer _iQualidade0a100) throws IOException {
        try {        //INICIAMOS COM A IMAGEM ORIGINAL ABERTA
            File imgFileOrig = _psFotoFile; //BUSCAMOS O ARQUIVO E ABRIMOS
            Bitmap b = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath());

            //TAMANHO ORIGINAL
            int origWidth = (b.getWidth() * _iProporcao) / 100;
            int origHeight = (b.getHeight() * _iProporcao) / 100;

                //SE É MAIORQ QUE O TAMANHO DESEJADO, CALCULAMOS O VALOR
                //ESCALAMOS A IMAGE E NÃO SÓ REDIMENSIONAMOS
                Bitmap b2 = Bitmap.createScaledBitmap(b, origWidth, origHeight, false);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                //COMPRESSA A IMAGEM PARA A QUALIDADE DESEJADA
                b2.compress(Bitmap.CompressFormat.JPEG, _iQualidade0a100, outStream);
                //SALVA O ARQUIVO E FECHAO MESMO

                _psFotoFile.createNewFile();
                //ESCREVE OS BITES NO ARQUIVO
                FileOutputStream fo = new FileOutputStream(_psFotoFile);
                fo.write(outStream.toByteArray());
                fo.close();
        }
        catch (Exception ex){

        }
    }

    public void prcAbreFoto_DoDiretorio(Activity activity, String _psCaminhoFoto)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri imgUri = Uri.parse("file://" + _psCaminhoFoto);
        intent.setDataAndType(imgUri, "image/*");
        activity.startActivity(intent);
    }

    public void prcAbreFoto_Bitmap(Activity activity, Bitmap _pbmpFoto)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("img", _pbmpFoto);
        activity.startActivity(intent);
    }

    public static Bitmap prcSQLite_RetornaFoto(byte[] _bFoto)
    {
        final Bitmap bmp = BitmapFactory.decodeByteArray(_bFoto, 0, _bFoto.length);
        return bmp;
    }

    public static byte[] prcSQLite_SalvaFoto(String _psCaminhoFoto) {

        FileInputStream fs = null;
        byte[] image = new byte[0];

        try {
            fs = new FileInputStream(_psCaminhoFoto);
            image = new byte[fs.available()];
            fs.read(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
