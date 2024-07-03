import crypto from "crypto-js";

const calculateSHA256 = (file: File): Promise<string> =>
{
    return new Promise((resolve, reject) =>
    {
        const reader = new FileReader();
        reader.onload = () =>
        {
            const hash = crypto.SHA256(crypto.enc.Latin1.parse(reader.result as string)).toString();
            resolve(hash);
        };
        reader.onerror = reject;
        reader.readAsBinaryString(file);
    });
};

export {
    calculateSHA256
}
