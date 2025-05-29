import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';
import { PDFDocument } from 'pdf-lib';

@Injectable({
    providedIn: 'root',
})
export class OcrService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINTS_BIOMETRICS = '/ocr';

    constructor(private httpClient: HttpClient) {}

    //GET
    recognizeText(
        file: File,
        biometer: string,
        side: string,
        idOrga: number[]
    ): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('file', file);
        formData.append('biometer', biometer);
        formData.append('side', side);
        idOrga.forEach((id) => {
            formData.append(`organizationsId`, id.toString());
        });

        return this.httpClient.post<any>(
            `${this.API_URL}${this.ENDPOINTS_BIOMETRICS}`,
            formData
        );
    }

    //PUT

    //POST

    //DELETE

    //Utils

    //Conversion PDF
    async convertImageToPdf(
        imageBlob: Blob,
        fileName: string
    ): Promise<File> {
        const minWidth = 300;
        const minHeight = 300;
        const maxWidth = 900;
        const maxHeight = 900;

        const resizedImageBlob = await this.resizeImage(
            imageBlob,
            minWidth,
            minHeight,
            maxWidth,
            maxHeight
        );
        const pdfDoc = await PDFDocument.create();
        let image;

        const arrayBuffer = await resizedImageBlob.arrayBuffer();
        const mimeType = resizedImageBlob.type;

        if (mimeType === 'image/png') {
            image = await pdfDoc.embedPng(arrayBuffer);
        } else if (mimeType === 'image/jpeg' || mimeType === 'image/jpg') {
            image = await pdfDoc.embedJpg(arrayBuffer);
        } else {
            throw new Error('Unsupported image type');
        }

        const page = pdfDoc.addPage([image.width, image.height]);
        page.drawImage(image, {
            x: 0,
            y: 0,
            width: image.width,
            height: image.height,
        });

        const pdfBytes = await pdfDoc.save();
        const pdfBlob = new Blob([pdfBytes], { type: 'application/pdf' });
        return new File([pdfBlob], fileName.replace(/\.[^/.]+$/, '.pdf'), {
            type: 'application/pdf',
        });
    }

    //Redimentionnement image
    private async resizeImage(
        imageBlob: Blob,
        minWidth: number,
        minHeight: number,
        maxWidth: number,
        maxHeight: number
    ): Promise<Blob> {
        return new Promise((resolve, reject) => {
            const img = new Image();
            const url = URL.createObjectURL(imageBlob);
            img.src = url;

            img.onload = () => {
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');

                let width = img.width;
                let height = img.height;

                if (width < minWidth || height < minHeight) {
                    const scale = Math.max(
                        minWidth / width,
                        minHeight / height
                    );
                    width = Math.floor(width * scale);
                    height = Math.floor(height * scale);
                } else if (width > maxWidth || height > maxHeight) {
                    const scale = Math.min(
                        maxWidth / width,
                        maxHeight / height
                    );
                    width = Math.floor(width * scale);
                    height = Math.floor(height * scale);
                }

                canvas.width = width;
                canvas.height = height;
                if (ctx) {
                    ctx.drawImage(img, 0, 0, width, height);

                    // Convert to grayscale
                    const imageData = ctx.getImageData(0, 0, width, height);
                    const data = imageData.data;
                    for (let i = 0; i < data.length; i += 4) {
                        const brightness =
                            0.34 * data[i] +
                            0.5 * data[i + 1] +
                            0.16 * data[i + 2];
                        data[i] = brightness;
                        data[i + 1] = brightness;
                        data[i + 2] = brightness;
                    }
                    ctx.putImageData(imageData, 0, 0);
                }

                canvas.toBlob((resizedBlob) => {
                    URL.revokeObjectURL(url);
                    if (resizedBlob) {
                        resolve(resizedBlob);
                    } else {
                        reject(new Error('Failed to resize image'));
                    }
                }, imageBlob.type);
            };

            img.onerror = () => {
                URL.revokeObjectURL(url);
                reject(new Error('Failed to load image'));
            };
        });
    }

}
