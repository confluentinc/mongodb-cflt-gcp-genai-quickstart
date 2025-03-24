import {useState, useRef, useEffect} from "react";
import {Bot, Send, X} from "lucide-react";
import {wsService} from "../utils/websocket";
import Markdown from "react-markdown";

interface ChatBoxProps {
    isOpen: boolean;
    onClose: () => void;
}

const ChatBox = ({isOpen, onClose}: ChatBoxProps) => {
    const [message, setMessage] = useState("");
    const [messages, setMessages] = useState<{ text: string; isUser: boolean }[]>([
        {
            text: "👋 **Welcome to MedGuide Health! I’m Your Virtual Doctor**\n\n" +
                "I’m here to help you explore possible medications for your symptoms in a safe and conversational way—just like a real doctor would.\n\n" +
                "⚠ **Important**: This is a **demo** and should be used for **demonstration purposes only**. If you’re feeling unwell or need medical advice, please consult a real doctor or healthcare professional.\n\n" +
                "💬 *Tell me what’s bothering you, and we’ll go from there! When did your symptoms begin?*\n",
            isUser: false
        }
    ]);
    const [isLoading, setIsLoading] = useState(false);
    const messagesEndRef = useRef<HTMLDivElement>(null);

    const scrollToBottom = () => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({behavior: "smooth", block: "end"});
        }
    };

    useEffect(() => {
        // Add a small delay to ensure the DOM has updated
        const timeoutId = setTimeout(scrollToBottom, 100);
        return () => clearTimeout(timeoutId);
    }, [messages, isLoading]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!message.trim()) return;

        setMessages([...messages, {text: message, isUser: true}]);
        setIsLoading(true);
        setMessage("");

        try {
            const response = await wsService.sendMessage(message);
            setMessages(prev => [...prev, {
                text: response,
                isUser: false
            }]);
        } catch (error) {
            setMessages(prev => [...prev, {
                text: "I apologize, but I'm having trouble connecting right now. Please try again later.",
                isUser: false
            }]);
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div
            className="fixed bottom-20 right-4 w-[700px] h-[500px] bg-white rounded-xl shadow-2xl animate-slide-up border border-gray-100">
            <div
                className="flex items-center justify-between bg-gradient-to-r from-medical-primary to-medical-accent text-white p-4 rounded-t-xl">
                <div className="flex items-center gap-3">
                    <div className="bg-white/20 p-2 rounded-full">
                        <Bot size={20}/>
                    </div>
                    <span className="font-semibold">Healthcare Assistant</span>
                </div>
                <button
                    onClick={onClose}
                    className="hover:bg-white/20 p-2 rounded-full transition-colors"
                >
                    <X size={20}/>
                </button>
            </div>

            <div className="h-[380px] overflow-y-auto p-4 space-y-4 bg-gray-50/50">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`flex ${msg.isUser ? "justify-end" : "justify-start"} animate-fade-in`}
                    >
                        <div
                            className={`max-w-[80%] p-3 rounded-2xl shadow-sm ${
                                msg.isUser
                                    ? "bg-medical-primary text-white"
                                    : "bg-white text-gray-800"
                            }`}
                            style={{whiteSpace: "pre-line"}}
                        >
                            <Markdown>{msg.text}</Markdown>
                        </div>
                    </div>
                ))}
                {isLoading && (
                    <div className="flex justify-start animate-fade-in">
                        <div className="bg-white p-3 rounded-2xl shadow-sm">
                            <div className="flex space-x-2">
                                <div className="w-2 h-2 bg-medical-primary rounded-full animate-bounce"
                                     style={{animationDelay: '0ms'}}></div>
                                <div className="w-2 h-2 bg-medical-primary rounded-full animate-bounce"
                                     style={{animationDelay: '150ms'}}></div>
                                <div className="w-2 h-2 bg-medical-primary rounded-full animate-bounce"
                                     style={{animationDelay: '300ms'}}></div>
                            </div>
                        </div>
                    </div>
                )}
                <div ref={messagesEndRef} className="h-0"/>
            </div>

            <form onSubmit={handleSubmit} className="p-4 border-t bg-white rounded-b-xl">
                <div className="flex gap-2">
                    <input
                        type="text"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        placeholder="Type your message..."
                        className="flex-1 p-3 border rounded-full focus:outline-none focus:ring-2 focus:ring-medical-primary/20 focus:border-medical-primary transition-all"
                    />
                    <button
                        type="submit"
                        className="bg-medical-primary text-white p-3 rounded-full hover:bg-medical-primary/90 transition-colors"
                        disabled={isLoading}
                    >
                        <Send size={18}/>
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ChatBox;